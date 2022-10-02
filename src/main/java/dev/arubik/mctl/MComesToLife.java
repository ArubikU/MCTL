package dev.arubik.mctl;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.entity.WorldEntityLoader;
import dev.arubik.mctl.events.ListenerLoader;
import dev.arubik.mctl.events.listeners.EntityListener;
import dev.arubik.mctl.events.listeners.GuiListener;
import dev.arubik.mctl.events.listeners.PacketListener;
import dev.arubik.mctl.events.listeners.PaperEvents;
import dev.arubik.mctl.holders.CommandHolder;
import dev.arubik.mctl.holders.EnabledPlugins;
import dev.arubik.mctl.holders.Nms;
import dev.arubik.mctl.holders.timers;
import dev.arubik.mctl.holders.Abstract.SkinHolder;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.CustomConfigurationSection;
import dev.arubik.mctl.utils.FileConfiguration;
import dev.arubik.mctl.utils.GuiCreator;
import dev.arubik.mctl.utils.Target;
import dev.arubik.mctl.utils.fileUtils;
import dev.arubik.mctl.utils.langFile;
import dev.arubik.mctl.utils.messageUtils;

public final class MComesToLife extends JavaPlugin {

    @Getter
    @Setter
    public static JavaPlugin plugin;

    @Getter
    public static Nms nms;

    @Getter
    private static String ServerVersion;

    public static FileConfiguration config;
    @Getter
    public static langFile proffesions;
    @Getter
    public static langFile messages;
    @Getter
    public static langFile villagers;
    @Getter
    public static langFile skins;
    @Getter
    public static langFile names;
    @Getter
    public static ListenerLoader loader;

    @Getter
    public static EnabledPlugins enabledPlugins;
    public static GuiCreator Maingui;
    @Getter
    public static SkinHolder skinHolder;

    @Override
    public org.bukkit.configuration.file.FileConfiguration getConfig() {
        return config.getConfig();
    }

    @Getter
    public static HashMap<String, Entity> lastClickedEntity = new HashMap<String, Entity>();

    @Override
    public void onEnable() {
        plugin = this;
        enabledPlugins = new EnabledPlugins();
        ServerVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
        proffesions = new langFile("proffesion.yml");
        config = fileUtils.getFileConfiguration("config.yml");
        messages = new langFile(config.getString("config.lang-file", "messages.yml"));
        skins = new langFile(config.getString("config.skins-file", "skins.yml"));
        names = new langFile(config.getString("config.names-file", "names.yml"));
        villagers = new langFile(config.getString("config.speech-file", "speech.yml"));
        skinHolder = new SkinHolder();
        nms = new Nms();
        skinHolder.preLoadSkins(config.getStringList("config.file-skins", new ArrayList<String>()));

        // register TabExecutor "holders/CommandHolder.java"

        // get all aliases from command mctl in plugin.yml
        String[] aliases = new String[] { "mctl", "mctolife", "mctol", "mct" };
        for (String alias : aliases) {
            this.getCommand(alias).setExecutor(new CommandHolder());
            this.getCommand(alias).setTabCompleter(new CommandHolder());
        }
        enabledPlugins = new EnabledPlugins();
        // register events
        loader = new ListenerLoader();
        loader.addListener(new EntityListener());
        loader.addListener(new GuiListener());
        loader.addListener(new PacketListener());
        try {
            if (Class.forName("io.papermc.paper.event.entity.EntityPortalReadyEvent") != null) {
                loader.addListener(new PaperEvents());
            }
        } catch (ClassNotFoundException e) {
            messageUtils.log("[MCTL] PaperEvents Listener dont register because is not paper server");
        }
        // loader.loadAllListeners();
        loader.RegisterListener();

        // load villagers from worlds
        WorldEntityLoader.makeVillagers();

        Maingui = new GuiCreator(
                fileUtils.getFileConfiguration(MComesToLife.config.getString("config.main-gui", "example-gui.yml"))
                        .getConfig().getConfigurationSection("gui"),
                MComesToLife.config.getString("config.main-gui", "example-gui.yml"));
        Maingui.setupInv();
        timers.startTimers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload() {
        enabledPlugins = new EnabledPlugins();
        proffesions = new langFile("proffesion.yml");
        config = fileUtils.getFileConfiguration("config.yml");
        messages = new langFile(config.getString("config.lang-file", "messages_en.yml"));
        skins = new langFile(config.getString("config.skins-file", "skins.yml"));
        names = new langFile(config.getString("config.names-file", "names_en.yml"));
        villagers = new langFile(config.getString("config.speech-file", "speech_en.yml"));
        skinHolder.preLoadSkins(config.getStringList("config.file-skins", new ArrayList<String>()));
        loader.removeListener();
        loader.RegisterListener();
        Maingui = new GuiCreator(
                fileUtils.getFileConfiguration(MComesToLife.config.getString("config.main-gui", "example-gui.yml"))
                        .getConfig().getConfigurationSection("gui"),
                MComesToLife.config.getString("config.main-gui", "example-gui.yml"));
        Maingui.setupInv();
    }

    public static Long getTimeOutProcreate() {
        ConfigurationSection conf = MComesToLife.config.getConfig().getConfigurationSection("config.procreate-timeout");
        int days = conf.getInt("days");
        int hours = conf.getInt("hours");
        int minutes = conf.getInt("minutes");
        int seconds = conf.getInt("seconds");
        // convert time to millis
        long time = (days * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
        return time;
    }

    public static Long getTimeFromConfig(CustomConfigurationSection conf) {
        int days = conf.getInt("days", 0);
        int hours = conf.getInt("hours", 0);
        int minutes = conf.getInt("minutes", 0);
        int seconds = conf.getInt("seconds", 0);
        // convert time to millis
        long time = (days * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
        return time;
    }

    public static Long getTimeFromArgs(int days, int hours, int minutes, int seconds) {
        long time = (days * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
        return time;
    }

    public static CustomVillager getlastClickedEntity(Player p) {
        Entity e = lastClickedEntity.get(p.getName());
        if (e == null) {
            if (DataMethods.isCustom(Target.getTargetEntity((Entity) p))) {
                CustomVillager vil = new CustomVillager((LivingEntity) Target.getTargetEntity((Entity) p));
                vil.loadVillager(false);
                return vil;
            }
        } else {
            if (DataMethods.isCustom(e)) {
                CustomVillager vil = new CustomVillager((LivingEntity) e);
                vil.loadVillager(false);
                return vil;
            }
        }
        return null;
    }

    public static HashMap<timeFormat, Integer> getTimeFromDate(long time) {
        HashMap<timeFormat, Integer> timeMap = new HashMap<timeFormat, Integer>();
        int days = (int) (time / (1000 * 60 * 60 * 24));
        int hours = (int) ((time / (1000 * 60 * 60)) % 24);
        int minutes = (int) ((time / (1000 * 60)) % 60);
        int seconds = (int) (time / 1000) % 60;
        timeMap.put(timeFormat.DAY, days);
        timeMap.put(timeFormat.HOURS, hours);
        timeMap.put(timeFormat.MINUTES, minutes);
        timeMap.put(timeFormat.SECONDS, seconds);
        return timeMap;
    }

    public enum timeFormat {
        DAY, HOURS, MINUTES, SECONDS
    }
}
