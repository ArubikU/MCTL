package dev.arubik.mctl.holders;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.mood;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.ItemSerializer;
import dev.arubik.mctl.utils.messageUtils;

public class VillagerInventoryHolder extends CustomVillager {

    private static ItemStack AIR;
    static {
        AIR = new ItemStack(org.bukkit.Material.AIR);
    }

    public VillagerInventoryHolder(LivingEntity v) {
        super(v);
    }

    public static VillagerInventoryHolder getInstance(CustomVillager v) {
        return new VillagerInventoryHolder(v.getLivingEntity());
    }

    private Inventory villagerInv;

    public void loadInventory() {
        this.loadVillager(false);
        this.setupInv();
        this.loadEquipment();
    }

    public void setupInv() {
        villagerInv = Bukkit.createInventory((InventoryHolder) this.villager, 54, "Inventory");
        villagerInv.addItem(this.getItems("inventory"));
    }

    public void equip(EquipmentSlot type, @Nullable ItemStack stack) {
        if (this.hasData(type.toString())) {
            ItemStack itemStack = ItemSerializer.read(this.getData(type.toString(), ""))[0];

            if (stack != null) {
                if (!ItemSerializer.getType(itemStack).contains("FLOWER")) {
                    this.villager.getWorld().dropItem(this.getLocation(), itemStack);
                }
            } else {
                this.villager.getWorld().dropItem(this.getLocation(), itemStack);
            }
            this.villager.getEquipment().setItem(type, null);
            this.setData(type.toString(), null);
        }
        if (stack == null)
            return;
        if (stack.getType().isAir())
            return;
        this.setData(type.toString(), ItemSerializer.write(stack));
        this.villager.getEquipment().setItem(type, stack);
        this.Disguise();
    }

    public Boolean deEquip(EquipmentSlot type) {
        if (this.hasData(type.toString())) {
            ItemStack itemStack = ItemSerializer.read(this.getData(type.toString(), ""))[0];
            if (!ItemSerializer.getType(itemStack).contains("FLOWER")) {
                this.villager.getWorld().dropItem(this.getLocation(), itemStack);
                this.villager.getEquipment().setItem(type, null);
                this.setData(type.toString(), null);
                return true;
            }
            return false;
        }
        return false;
    }

    public void loadEquipment() {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (this.hasData(slot.toString())) {
                ItemStack stack = ItemSerializer.read(this.getData(slot.toString(), ""))[0];
                this.villager.getEquipment().setItem(slot, stack);
            }
        }
    }

    public PlayerDisguise loadDisguiseDisplay(PlayerDisguise p) {
        for (String key : this.getData().keySet()) {
            if (EnumContains(key)) {
                ItemStack stack = ItemSerializer.read(this.getData(key, ""))[0];
                p.getWatcher().setItemStack(EquipmentSlot.valueOf(key), stack);
            }
        }
        return p;
    }

    public Boolean EnumContains(String a) {
        for (EquipmentSlot b : EquipmentSlot.values()) {
            if (b.toString().equalsIgnoreCase(a)) {
                return true;
            }
        }
        return false;
    }

    public EquipmentSlot getEnum(String a) {
        for (EquipmentSlot b : EquipmentSlot.values()) {
            if (b.toString().equalsIgnoreCase(a)) {
                return b;
            }
        }
        return EquipmentSlot.HEAD;
    }

    public void giveItem(Player gifter) {
        ItemStack stack = gifter.getInventory().getItemInMainHand();
        gifter.getInventory().setItemInMainHand(AIR);
        if (Material.contains(ItemSerializer.getType(stack))) {
            if (Material.isType(Material.HELMET, ItemSerializer.getType(stack))) {
                if (stack.getAmount() > 1) {
                    ItemStack clone = stack;
                    clone.setAmount(stack.getAmount() - 1);
                    stack.setAmount(1);
                    this.dropItems(clone);
                }
                equip(EquipmentSlot.HEAD, stack);
            }
            if (Material.isType(Material.CHESTPLATE, ItemSerializer.getType(stack))) {
                if (stack.getAmount() > 1) {
                    ItemStack clone = stack;
                    clone.setAmount(stack.getAmount() - 1);
                    stack.setAmount(1);
                    this.dropItems(clone);
                }
                equip(EquipmentSlot.CHEST, stack);
            }
            if (Material.isType(Material.LEGGINGS, ItemSerializer.getType(stack))) {
                if (stack.getAmount() > 1) {
                    ItemStack clone = stack;
                    clone.setAmount(stack.getAmount() - 1);
                    stack.setAmount(1);
                    this.dropItems(clone);
                }
                equip(EquipmentSlot.LEGS, stack);
            }
            if (Material.isType(Material.BOOTS, ItemSerializer.getType(stack))) {
                if (stack.getAmount() > 1) {
                    ItemStack clone = stack;
                    clone.setAmount(stack.getAmount() - 1);
                    stack.setAmount(1);
                    this.dropItems(clone);
                }
                equip(EquipmentSlot.FEET, stack);
            }
            if (Material.isTool(ItemSerializer.getType(stack))) {
                if (stack.getAmount() > 1) {
                    ItemStack clone = stack;
                    clone.setAmount(stack.getAmount() - 1);
                    stack.setAmount(1);
                    this.dropItems(clone);
                }
                equip(EquipmentSlot.HAND, stack);
            } else {
                if (stack.getAmount() > 1 && !(ItemSerializer.getType(stack).contains("ARROW"))) {
                    ItemStack clone = stack;
                    clone.setAmount(stack.getAmount() - 1);
                    stack.setAmount(1);
                    this.dropItems(clone);
                }
                equip(EquipmentSlot.OFF_HAND, stack);
            }

            return;
        } else {
            String useProffessionForGifts = "";

            if (MComesToLife.config.getBoolean("config.proffesion-gifts", false)) {
                useProffessionForGifts = "." + this.getData("type", "nitwit") + ".";
            }

            if (MComesToLife.config
                    .Contains("config.gifts." + useProffessionForGifts + ItemSerializer.getType(stack))) {
                Integer extraLikes = MComesToLife.config.getInteger("config.gifts." + ItemSerializer.getType(stack), 0);
                Integer multiplier = 1;
                if (stack.getAmount() > 4) {
                    multiplier = Math.round(stack.getAmount() / 4);
                }
                if (extraLikes < 0) {
                    extraLikes *= -1;
                    this.takeLikes(extraLikes, gifter);
                    this.takeHappiness(3);
                    if (extraLikes > 10) {

                        messageUtils.MessageParsedPlaceholders((CommandSender) gifter, new Message(mood
                                .getText("gift-bad", DataMethods.getFamily("", gifter, villager))),
                                this);
                    } else {
                        messageUtils.MessageParsedPlaceholders((CommandSender) gifter, new Message(mood
                                .getText("gift-small", DataMethods.getFamily("", gifter, villager))),
                                this);
                    }

                    return;
                } else {
                    this.addLikes(extraLikes * multiplier, gifter);
                    this.addHappiness(3);
                    if (extraLikes > 25) {
                        messageUtils.MessageParsedPlaceholders((CommandSender) gifter, new Message(mood
                                .getText("gift-love", DataMethods.getFamily("", gifter, villager))),
                                this);
                    } else if (extraLikes > 10) {
                        messageUtils.MessageParsedPlaceholders((CommandSender) gifter, new Message(mood
                                .getText("gift-great", DataMethods.getFamily("", gifter, villager))),
                                this);
                    } else {
                        messageUtils.MessageParsedPlaceholders((CommandSender) gifter, new Message(mood
                                .getText("gift-regular", DataMethods.getFamily("", gifter, villager))),
                                this);
                    }
                }
                villagerInv.addItem(stack);
                return;
            }
            if (MComesToLife.config.Contains("config.rings." + ItemSerializer.getType(stack))) {
                Integer extraLikes = MComesToLife.config.getInteger("config.rings." + ItemSerializer.getType(stack), 0);

                if (stack.getAmount() > 1) {
                    ItemStack clone = stack;
                    clone.setAmount(stack.getAmount() - 1);
                    stack.setAmount(1);
                    this.dropItems(clone);
                }

                DataMethods.marry(villager, gifter, extraLikes);
                villagerInv.addItem(stack);
                return;
            }
            if (MComesToLife.config.Contains("config.divorce." + ItemSerializer.getType(stack))) {
                Integer extraLikes = MComesToLife.config.getInteger("config.divorce." + ItemSerializer.getType(stack),
                        0);

                if (stack.getAmount() > 1) {
                    ItemStack clone = stack;
                    clone.setAmount(stack.getAmount() - 1);
                    stack.setAmount(1);
                    this.dropItems(clone);

                    for (ItemStack a : villagerInv.getContents()) {

                        if (MComesToLife.config.Contains("config.rings." + ItemSerializer.getType(a))) {
                            villagerInv.remove(a);
                            SaveInventory();
                            dropItems(a);
                        }
                    }
                }

                divorce(gifter);
                // villagerInv.addItem(stack);
                return;
            }

            villagerInv.addItem(stack).forEach((a, result) -> {
                this.dropItems(result);
            });
        }
    }

    public void SaveInventory() {
        this.putItems("inventory", villagerInv.getContents());
    }

    public void DropInventory() {
        for (EquipmentSlot type : EquipmentSlot.values()) {
            equip(type, null);
        }
        this.dropItems(villagerInv.getStorageContents());
        villagerInv.clear();
        SaveInventory();
    }

    public enum Material {
        HELMET, CHESTPLATE, LEGGINGS, BOOTS, SWORD, AXE, PICKAXE, SHOVEL, HOE, BOW, SHIELD, ARROW, TOTEM;

        public static Boolean contains(String a) {
            for (Material b : Material.values()) {
                if (a.toUpperCase().contains(b.toString().toUpperCase())) {
                    return true;
                }
            }
            return false;
        }

        public static Boolean isType(Material mat, String type) {
            if (type.toUpperCase().contains(mat.toString())) {
                return true;
            }
            return false;
        }

        public static Boolean isTool(String type) {
            type = type.toUpperCase();
            return type.contains("AXE") || type.contains("SWORD") || type.contains("SHOVEL") || type.contains("HOE")
                    || type.contains("BOW") || type.contains("PICKAXE") || type.contains("ROD")
                    || type.contains("WEAPON");
        }
    }
}
