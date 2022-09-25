package dev.arubik.mctl.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.utils.lib.lang3.math.NumberUtils;
import lombok.Getter;
import lombok.Setter;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.TypeAction;
import dev.arubik.mctl.holders.Message;

public class GuiCreator {
    private static ArrayList<Integer> avaliableSize = new ArrayList<Integer>();
    private ConfigurationSection config;
    private CustomConfigurationSection Customconfig;
    private String path;
    private Inventory inv;
    @Getter
    public GuiHolder Gui;
    public HashMap<Integer, ItemStack> InventoryItems = new HashMap<Integer, ItemStack>();

    public GuiCreator(ConfigurationSection config, String path) {
        this.config = config;
        this.path = path;
        Customconfig = new CustomConfigurationSection(config);
        avaliableSize.add(9);
        avaliableSize.add(18);
        avaliableSize.add(28);
        avaliableSize.add(37);
        avaliableSize.add(45);
    }

    public class GuiHolder implements InventoryHolder {
        @Setter
        private Inventory Inventory;

        @Getter
        public HashMap<String, Object> InventoryData = new HashMap<String, Object>();

        public <T> T putInventoryData(String key, T value) {
            InventoryData.put(key, value);
            return value;
        }

        @Setter
        @Getter
        private String type;

        @Setter
        @Getter
        private String path;

        @Setter
        @Getter
        private String section;

        @Override
        public Inventory getInventory() {
            return this.Inventory;
        }

    }

    public void setupInv() {
        GuiHolder holder = new GuiHolder();
        holder.setType(Customconfig.getString("id", UUID.randomUUID().toString()));
        holder.setSection(config.getCurrentPath());
        holder.setPath(path);
        if (avaliableSize.contains(Customconfig.getInteger(new String[] { "slots", "size" }, 9))) {
            inv = Bukkit.createInventory(holder, Customconfig.getInt(new String[] { "slots", "size" }, 9),
                    Customconfig.getString(new String[] { "gui-name", "title" }, "Title"));
        } else {
            messageUtils.log("<red>[MCTL]</red> <gray> The gui with path " + config.getCurrentPath()
                    + " dont have a correct size");
        }
        this.Gui = holder;
        loadItem();
    }

    public void loadItem() {
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            if (NumberUtils.isNumber(key)) {
                try {
                    int slot = Integer.parseInt(key);
                    if (slot <= inv.getSize()) {
                        this.InventoryItems.put(slot, ItemSerializer
                                .getFromConfigurationSection(config.getConfigurationSection("items." + key)));
                    } else {
                        messageUtils.log("<red>[MCTL]</red> <gray> The gui with path " + config.getCurrentPath()
                                + " have a item with a slot bigger than the size of the gui");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public enum Action {
        OPEN_MENU,
        PLAYER_COMMAND,
        CONSOLE_COMMAND,
        INTERACT,
        DIVORCE,
        TRADE,
        DEEQUIP

    }

    public void OpenInv(Player... players) {
        for (Player player : players) {
            String tittle = messageUtils.StringParsedPlaceholders(player,
                    new Message(Customconfig.getString(new String[] { "gui_name", "title" }, "Title")),
                    MComesToLife.getlastClickedEntity(player));
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                tittle = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, tittle);
            }
            tittle = new Message(tittle).removeMiniMessage().getString();
            Inventory clone = Bukkit.createInventory(Gui, inv.getSize(), tittle);
            InventoryItems.forEach((slot, item) -> {
                clone.setItem(slot, ItemSerializer.generateItem(item, player));
            });
            player.openInventory(clone);
        }
    }

}
