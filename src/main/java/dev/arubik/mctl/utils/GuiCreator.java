package dev.arubik.mctl.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.inventory.Slot;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.TypeAction;
import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.holders.Methods.DataMethods;

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
        avaliableSize.add(54);
    }

    public class GuiHolder implements InventoryHolder {
        @Setter
        private Inventory Inventory;

        @Setter
        @Getter
        private ConfigurationSection config;

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

        public GuiCreator regenInventory() {
            return new GuiCreator(config, path);
        }

    }

    public void setupInv() {
        GuiHolder holder = new GuiHolder();
        holder.setType(Customconfig.getString("id", UUID.randomUUID().toString()));
        holder.setSection(config.getCurrentPath());
        holder.setPath(path);
        holder.setConfig(config);
        if (avaliableSize.contains(Customconfig.getInteger(new String[] { "slots", "size" }, 9))) {
            inv = Bukkit.createInventory(holder, Customconfig.getInt(new String[] { "slots", "size" }, 9),
                    Customconfig.getString(new String[] { "gui-name", "title" }, "Title"));
            this.Gui = holder;
            loadItem();
        } else {
            MessageUtils.log("<red>[MCTL]</red> <gray> The gui with path " + config.getCurrentPath()
                    + " dont have a correct size");
        }
    }

    @Nullable
    public ItemStack getSlot(EquipmentSlot slot, @Nullable CustomVillager v) {
        if (v == null)
            return null;
        if (v.hasData(slot.toString())) {
            if (ItemSerializer.read(v.getData(slot.toString(), "")).length == 0) {
                return null;
            }
            ItemStack stack = ItemSerializer.read(v.getData(slot.toString(), ""))[0];
            return stack;
        }
        if (v.getLivingEntity().getEquipment().getItem(slot) != null) {
            return v.getLivingEntity().getEquipment().getItem(slot);
        }
        return null;
    }

    public void loadItem() {
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            if (NumberUtils.isCreatable(key)) {
                try {
                    int slot = Integer.parseInt(key);
                    if (slot <= inv.getSize()) {
                        this.InventoryItems.put(slot, ItemSerializer
                                .getFromConfigurationSection(config.getConfigurationSection("items." + key)));
                    } else {
                        MessageUtils.log("<red>[MCTL]</red> <gray> The gui with path " + config.getCurrentPath()
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
        DEEQUIP,
        DROPITEMFROMINVENTORY,
        INTERNAL

    }

    public void OpenInv(Player... players) {
        if (inv == null) {

            MessageUtils.log("<red>[MCTL]</red> <gray> The gui with path " + config.getCurrentPath()
                    + " have errors in config");
        }
        for (Player player : players) {
            String tittle = MessageUtils.StringParsedPlaceholders(player,
                    new Message(Customconfig.getString(new String[] { "gui_name", "title" }, "Title")),
                    DataMethods.getlastClickedEntity(player));
            if (MComesToLife.getEnabledPlugins().isEnabled("PlaceholderAPI")) {
                tittle = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, tittle);
            }
            tittle = new Message(tittle).removeMiniMessage().getString();
            Inventory clone = Bukkit.createInventory(Gui, inv.getSize(), tittle);
            InventoryItems.forEach((slot, tempItem) -> {
                ItemStack item = tempItem.clone();
                if (config.getConfigurationSection("items." + slot).contains("slot")) {
                    if (DataMethods.getlastClickedEntity(player) != null) {
                        switch (config.getConfigurationSection("items." + slot).getString("slot").toUpperCase()) {
                            case "HAND": {
                                item = getSlot(EquipmentSlot.HAND, DataMethods.getlastClickedEntity(player));
                                break;
                            }
                            case "OFFHAND": {
                                item = getSlot(EquipmentSlot.OFF_HAND, DataMethods.getlastClickedEntity(player));
                                break;
                            }
                            case "LEGS": {
                                item = getSlot(EquipmentSlot.LEGS, DataMethods.getlastClickedEntity(player));
                                break;
                            }
                            case "BOOTS": {
                                item = getSlot(EquipmentSlot.FEET, DataMethods.getlastClickedEntity(player));
                                break;
                            }
                            case "HELMET": {
                                item = getSlot(EquipmentSlot.HEAD, DataMethods.getlastClickedEntity(player));
                                break;
                            }
                            case "CHESTPLATE": {
                                item = getSlot(EquipmentSlot.CHEST, DataMethods.getlastClickedEntity(player));
                                break;
                            }
                        }

                        if (NumberUtils
                                .isCreatable(config.getConfigurationSection("items." + slot).getString("slot"))) {
                            int slot2 = Integer
                                    .parseInt(config.getConfigurationSection("items." + slot).getString("slot"));
                            VillagerInventoryHolder holder = VillagerInventoryHolder
                                    .getInstance(DataMethods.getlastClickedEntity(player));
                            holder.loadInventoryNoReload();
                            item = holder.getItem(slot2);

                        }
                        if (item == null) {
                            if (config.getConfigurationSection("items." + slot).get("replace-armor-item") != null) {
                                item = ItemSerializer.getFromConfigurationSection(
                                        config.getConfigurationSection("items." + slot + ".replace-armor-item"));
                                item = ItemSerializer.putData(item, "armor_replace_section",
                                        config.getConfigurationSection("items." + slot).get("replace-armor-item"));
                            } else {
                                item = tempItem;
                            }
                        } else {
                            if (config.getConfigurationSection("items." + slot).get("add-lore") != null
                                    && item.hasItemMeta()) {
                                List<String> addLore = (List<String>) config
                                        .getConfigurationSection("items." + slot).getList("add-lore");
                                List<String> lore = item.getItemMeta().getLore();
                                lore.addAll(addLore);
                                ItemMeta meta = item.getItemMeta();
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                            }
                            if (config.getConfigurationSection("items." + slot).get("rename") != null) {
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(
                                        config.getConfigurationSection("items." + slot).getString("rename"));
                                item.setItemMeta(meta);
                            }
                        }
                    }
                }
                if (config.getConfigurationSection("items." + slot).contains("conditions")) {
                    List<String> conditions = config.getConfigurationSection("items." + slot)
                            .getStringList("conditions");
                    for (String condition : conditions) {
                        ConditionReader cond = new ConditionReader(condition);
                        cond.redoConditions(player);
                        if (MComesToLife.isDEBUG()) {
                            MessageUtils.BukkitLog("condition: " + cond.getLine());
                        }
                        if (cond.isAvaliableCondition()) {
                            if (MComesToLife.isDEBUG()) {
                                MessageUtils.BukkitLog("condition Detected: true");
                            }
                            if (cond.checkCondition(player, config.getConfigurationSection("items." + slot))) {
                                if (MComesToLife.isDEBUG()) {
                                    MessageUtils.BukkitLog("condition Pass: false");
                                }
                                item = new ItemStack(Material.AIR);
                                if (cond.get("replace-item") != null) {
                                    item = ItemSerializer.getFromConfigurationSection(
                                            config.getConfigurationSection(
                                                    "items." + slot + "." + cond.get("replace-item")));
                                    item = ItemSerializer.putData(item, "condition_section", cond.get("replace-item"));
                                }
                            } else {
                                if (MComesToLife.isDEBUG()) {
                                    MessageUtils.BukkitLog("condition Pass: true");
                                }
                            }
                            item = ItemSerializer.putData(item, "conditioned", true);
                        }
                    }
                }
                if (!item.getType().isAir()) {
                    clone.setItem(slot, ItemSerializer.generateItem(item, player));
                }
            });
            player.openInventory(clone);
        }
    }

}
