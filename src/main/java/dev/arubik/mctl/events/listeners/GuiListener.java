package dev.arubik.mctl.events.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.TypeAction;
import dev.arubik.mctl.events.Listener;
import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.CustomConfigurationSection;
import dev.arubik.mctl.utils.GuiCreator;
import dev.arubik.mctl.utils.ItemSerializer;
import dev.arubik.mctl.utils.fileUtils;
import dev.arubik.mctl.utils.messageUtils;
import dev.arubik.mctl.utils.GuiCreator.Action;
import dev.arubik.mctl.utils.GuiCreator.GuiHolder;

public class GuiListener extends Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getHolder() instanceof GuiHolder) {
            event.setCancelled(true);

            if(event.getClickedInventory().getItem(event.getSlot())==null)return;
            if(event.getClickedInventory().getItem(event.getSlot()).getType().toString().contains("AIR"))return;
            

            GuiHolder holder = (GuiHolder) event.getClickedInventory().getHolder();
            ConfigurationSection conf = fileUtils.getFileConfiguration(holder.getPath()).getConfig()
                    .getConfigurationSection(holder.getSection());
            if (conf.getConfigurationSection("items").contains(Integer.toString(event.getSlot()))) {
                ConfigurationSection item = conf.getConfigurationSection("items")
                        .getConfigurationSection(Integer.toString(event.getSlot()));

                if(ItemSerializer.containsData(event.getClickedInventory().getItem(event.getSlot()), "conditioned")){
                    if(ItemSerializer.containsData(event.getClickedInventory().getItem(event.getSlot()), "condition_section")){
                        if(!item.contains(ItemSerializer.getData(event.getClickedInventory().getItem(event.getSlot()), "condition_section",String.class)))return;
                item = conf.getConfigurationSection("items")
                .getConfigurationSection(Integer.toString(event.getSlot())).getConfigurationSection(ItemSerializer.getData(event.getClickedInventory().getItem(event.getSlot()), "condition_section",String.class));
                    }else{
                        return;
                    }
                    
                }

                if(item == null) return;

                messageUtils.Bukkitlog("InventoryClick: " + event.getClick().toString());
                if (item.getConfigurationSection("ON_" + event.getClick().toString() + "_CLICK") == null)
                    return;
                for (String key : item.getConfigurationSection("ON_" + event.getClick().toString() + "_CLICK")
                        .getKeys(false)) {
                    if (Action.valueOf(key.toUpperCase().replaceAll("[0-9]+", "")) != null) {
                        Action action = Action.valueOf(key.toUpperCase().replaceAll("[0-9]+", ""));
                        switch (action) {
                            case CONSOLE_COMMAND: {
                                CustomConfigurationSection actionConfig = new CustomConfigurationSection(item
                                        .getConfigurationSection(
                                                "ON_" + event.getClick().toString() + "_CLICK." + key));
                                String[] slots = new String[] { "command", "cmd", "dispatch" };
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                                        messageUtils.StringParsedPlaceholders(event.getWhoClicked(),
                                                new Message(actionConfig.getString(slots, "sudo %player% say Hey"))));

                                break;
                            }
                            case OPEN_MENU: {
                                ConfigurationSection actionConfig = item
                                        .getConfigurationSection("ON_" + event.getClick().toString() + "_CLICK." + key);
                                if (actionConfig.contains("RUTE") && actionConfig.contains("PATH")) {
                                    if (fileUtils.getFileConfiguration(actionConfig.getString("RUTE")).getConfig()
                                            .contains(actionConfig.getString("PATH"))) {
                                        GuiCreator guiToOpen = new GuiCreator(
                                                fileUtils.getFileConfiguration(actionConfig.getString("RUTE"))
                                                        .getConfig()
                                                        .getConfigurationSection(actionConfig.getString("PATH")),
                                                actionConfig.getString("RUTE"));
                                        List<String> data = new CustomConfigurationSection(
                                                fileUtils.getFileConfiguration(actionConfig.getString("RUTE"))
                                                        .getConfig()
                                                        .getConfigurationSection(actionConfig.getString("PATH")))
                                                .getStringList("saved-data", new ArrayList<String>());
                                        guiToOpen.setupInv();
                                        data.forEach(saved -> {
                                            switch (saved.toUpperCase()) {
                                                case "PLAYER": {
                                                    guiToOpen.Gui.putInventoryData("PLAYER",
                                                            (Player) event.getWhoClicked());
                                                }
                                                case "CUSTOMVILLAGER": {
                                                    if (MComesToLife.lastClickedEntity.containsKey(
                                                            event.getWhoClicked().getUniqueId().toString())) {
                                                        guiToOpen.Gui.putInventoryData("CUSTOMVILLAGER",
                                                                MComesToLife.getlastClickedEntity(
                                                                        (Player) event.getWhoClicked()));
                                                    }
                                                }
                                            }
                                        });
                                        guiToOpen.OpenInv((Player) event.getWhoClicked());

                                    }
                                }
                                break;
                            }
                            case PLAYER_COMMAND: {
                                CustomConfigurationSection actionConfig = new CustomConfigurationSection(item
                                        .getConfigurationSection(
                                                "ON_" + event.getClick().toString() + "_CLICK." + key));
                                String[] slots = new String[] { "command", "cmd", "dispatch" };
                                Bukkit.getServer().dispatchCommand(event.getWhoClicked(),
                                        messageUtils.StringParsedPlaceholders(event.getWhoClicked(),
                                                new Message(actionConfig.getString(slots, "sudo %player% say Hey"))));

                                break;
                            }
                            case TRADE: {
                                // verify if the slot contains a item
                                if (!event.getWhoClicked().hasPermission("mctl.villager.trade")
                                        && !event.getWhoClicked().isOp()) {
                                    return;
                                }
                                if (event.getInventory().getItem(event.getSlot()) != null) {
                                    if (event.getInventory().getItem(event.getSlot()).getType() != Material.AIR) {
                                        if (MComesToLife.lastClickedEntity
                                                .containsKey(event.getWhoClicked().getUniqueId())) {
                                            if (MComesToLife.lastClickedEntity
                                                    .get(event.getWhoClicked().getUniqueId()) instanceof Villager) {
                                                Villager vil = (Villager) MComesToLife.lastClickedEntity
                                                        .get(event.getWhoClicked().getUniqueId());
                                                event.getWhoClicked().openInventory(vil.getInventory());
                                            }
                                        }

                                    }
                                }
                            }
                            case INTERACT: {
                                CustomConfigurationSection actionConfig = new CustomConfigurationSection(item
                                        .getConfigurationSection(
                                                "ON_" + event.getClick().toString() + "_CLICK." + key));
                                if (TypeAction.valueOf(actionConfig.getString("action", "CHAT")) != null) {
                                    if (actionConfig.getBoolean(key, false)) {
                                        // get last clicked villager
                                        if (holder.InventoryData.containsKey("CUSTOMVILLAGER")) {
                                            CustomVillager villager = (CustomVillager) holder.InventoryData
                                                    .get("CUSTOMVILLAGER");
                                            villager.loadVillager(false);
                                            villager.getMood().speech(villager,
                                                    TypeAction.valueOf(actionConfig.getString("action", "CHAT"))
                                                            .toString().toLowerCase(),
                                                    event.getWhoClicked());
                                        } else if (MComesToLife.lastClickedEntity
                                                .containsKey(event.getWhoClicked().getUniqueId().toString())) {
                                            CustomVillager villager = (CustomVillager) MComesToLife.lastClickedEntity
                                                    .get(event.getWhoClicked().getUniqueId().toString());
                                            villager.loadVillager(false);
                                            villager.getMood().speech(villager,
                                                    TypeAction.valueOf(actionConfig.getString("action", "CHAT"))
                                                            .toString().toLowerCase(),
                                                    event.getWhoClicked());
                                        }
                                    }
                                }
                                break;
                            }
                            case DIVORCE: {
                                CustomConfigurationSection actionConfig = new CustomConfigurationSection(item
                                        .getConfigurationSection(
                                                "ON_" + event.getClick().toString() + "_CLICK." + key));
                                if (actionConfig.getBoolean(key, false)) {
                                    // get last clicked villager
                                    CustomVillager villager = null;
                                    if (holder.InventoryData.containsKey("CUSTOMVILLAGER")) {
                                        villager = new CustomVillager(
                                                (LivingEntity) holder.InventoryData.get("CUSTOMVILLAGER"));
                                    } else if (MComesToLife.lastClickedEntity
                                            .containsKey(event.getWhoClicked().getUniqueId().toString())) {
                                        villager = new CustomVillager((LivingEntity) MComesToLife.lastClickedEntity
                                                .get(event.getWhoClicked().getUniqueId().toString()));
                                    }
                                    if (villager != null) {
                                        if (DataMethods.getFamily("", (Player) event.getWhoClicked(), villager.villager)
                                                .equalsIgnoreCase("spouse")) {
                                            villager.divorce(event.getWhoClicked());
                                        }
                                    }
                                }
                            }
                            case DEEQUIP: {
                                CustomConfigurationSection actionConfig = new CustomConfigurationSection(item
                                        .getConfigurationSection(
                                                "ON_" + event.getClick().toString() + "_CLICK." + key));

                                VillagerInventoryHolder villager = VillagerInventoryHolder
                                        .getInstance((CustomVillager) holder.InventoryData
                                                .get("CUSTOMVILLAGER"));
                                villager.loadVillager(false);
                                if (villager.EnumContains(actionConfig.getString("slot", "HAND"))) {
                                    villager.loadInventory();
                                    villager.deEquip(villager.getEnum(actionConfig.getString("slot", "HAND")));
                                }

                            }
                            default:
                                break;

                        }
                    }
                }
            }
        }
    }

}
