package dev.arubik.mctl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;
import dev.arubik.mctl.entity.WorldEntityLoader;
import dev.arubik.mctl.enums.Mood;
import dev.arubik.mctl.events.ListenerLoader;
import dev.arubik.mctl.events.listeners.EntityListener;
import dev.arubik.mctl.events.listeners.GuiListener;
import dev.arubik.mctl.events.listeners.PacketListener;
import dev.arubik.mctl.events.listeners.PaperEvents;
import dev.arubik.mctl.holders.CommandHolder;
import dev.arubik.mctl.holders.EnabledPlugins;
import dev.arubik.mctl.holders.EntityAi;
import dev.arubik.mctl.holders.Nms;
import dev.arubik.mctl.holders.WaitTimePerAction;
import dev.arubik.mctl.holders.Timers;
import dev.arubik.mctl.holders.Abstract.SkinHolder;
import dev.arubik.mctl.placeholderApi.MarryPlaceholder;
import dev.arubik.mctl.placeholderApi.PlaceholderBase;
import dev.arubik.mctl.placeholderApi.VillagerPlaceholder;
import dev.arubik.mctl.utils.FileConfiguration;
import dev.arubik.mctl.utils.GuiCreator;
import dev.arubik.mctl.utils.FileUtils;
import dev.arubik.mctl.utils.LangFile;
import dev.arubik.mctl.utils.MessageUtils;

public final class MComesToLife extends JavaPlugin {

    private static JavaPlugin plugin;

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static List<PlaceholderBase> getPlaceholders() {
        return placeholders;
    }

    public static Nms getNms() {
        return nms;
    }

    public static String getServerVersion() {
        return ServerVersion;
    }

    public static LangFile getProffesions() {
        return proffesions;
    }

    public static LangFile getMessages() {
        return messages;
    }

    public static LangFile getVillagers() {
        return villagers;
    }

    public static LangFile getSkins() {
        return skins;
    }

    public static LangFile getNames() {
        return names;
    }

    public static ListenerLoader getLoader() {
        return loader;
    }

    public static boolean isPAPER() {
        return PAPER;
    }

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static GuiCreator getMaingui() {
        return Maingui;
    }

    public static EnabledPlugins getEnabledPlugins() {
        return enabledPlugins;
    }

    public static SkinHolder getSkinHolder() {
        return skinHolder;
    }

    private static List<PlaceholderBase> placeholders = new ArrayList<>();
    public static Nms nms;
    private static String ServerVersion;
    private static FileConfiguration config;
    public static LangFile proffesions;
    public static LangFile messages;
    public static LangFile villagers;
    public static LangFile skins;
    public static LangFile names;
    public static ListenerLoader loader;

    public static boolean PAPER = false;
    public static boolean DEBUG = false;

    public static EnabledPlugins enabledPlugins;
    public static GuiCreator Maingui;

    public static SkinHolder skinHolder;

    public static FileConfiguration getMainConfig() {
        return config;
    }

    @Override
    public org.bukkit.configuration.file.FileConfiguration getConfig() {
        return config.getConfig();
    }

    @Getter
    public static HashMap<String, Entity> lastClickedEntity = new HashMap<String, Entity>();

    public void loadCompatibilities() {
        if (!config.getConfig().contains("config.compatibility"))
            return;
        config.getConfig().getConfigurationSection("config.compatibility").getKeys(false).forEach(key -> {
            if (!config.getBoolean("config.compatibility." + key, true))
                enabledPlugins.forceDisable(key);
        });
    }

    public void addPlaceholders() {
        new VillagerPlaceholder().register();
        new MarryPlaceholder().register();
    }

    @Override
    public void onEnable() {
        plugin = this;
        ServerVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
        load();
        String[] aliases = new String[] { "mctl", "mctolife", "mctol", "mct" };
        for (String alias : aliases) {
            this.getCommand(alias).setExecutor(new CommandHolder());
            this.getCommand(alias).setTabCompleter(new CommandHolder());
        }
        loader = new ListenerLoader();
        loader.addListener(new EntityListener());
        loader.addListener(new GuiListener());
        loader.addListener(new PacketListener());
        try {
            if (Class.forName("io.papermc.paper.event.entity.EntityPortalReadyEvent") != null) {
                PAPER = true;
                loader.addListener(new PaperEvents());
            }
        } catch (ClassNotFoundException e) {
            MessageUtils.log("[MCTL] PaperEvents Listener dont register because is not paper server");
        }
        loader.RegisterListener();
        WorldEntityLoader.makeVillagers();
        Timers.startTimers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void load() {
        enabledPlugins = new EnabledPlugins();
        proffesions = new LangFile("proffesion.yml");
        config = FileUtils.getFileConfiguration("config.yml");
        messages = new LangFile(config.getString("config.lang-file", "messages_en.yml"));
        skins = new LangFile(config.getString("config.skins-file", "skins.yml"));
        names = new LangFile(config.getString("config.names-file", "names_en.yml"));
        villagers = new LangFile(config.getString("config.speech-file", "speech_en.yml"));
        skinHolder = new SkinHolder();
        nms = new Nms();
        DEBUG = MComesToLife.getMainConfig().getBoolean("config.debug", false);
        loadCompatibilities();
        for (PlaceholderBase pch : getPlaceholders()) {
            pch.register();
        }

        Maingui = new GuiCreator(
                FileUtils.getFileConfiguration(MComesToLife.config.getString("config.main-gui", "example-gui.yml"))
                        .getConfig().getConfigurationSection("gui"),
                MComesToLife.config.getString("config.main-gui", "example-gui.yml"));
        Maingui.setupInv();
        Mood.waitTimes = new WaitTimePerAction("interact");
        skinHolder.preLoadSkins(config.getStringList("config.file-skins", new ArrayList<String>()));
        EntityAi.reload();
    }

    public void reload() {
        unload();
        load();
        loader.RegisterListener();
    }

    public void unload() {
        loader.removeListener();
        for (PlaceholderBase pch : getPlaceholders()) {
            if (pch.isRegistered()) {
                pch.unregister();
            }
        }
    }
}
