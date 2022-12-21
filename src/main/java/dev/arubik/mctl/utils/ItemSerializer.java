package dev.arubik.mctl.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import io.lumine.mythic.bukkit.compatibility.MMOItemsSupport;
import io.lumine.mythic.lib.adventure.text.Component;
import io.lumine.mythic.lib.adventure.text.TextComponent;
import io.lumine.mythic.lib.adventure.text.serializer.legacy.LegacyComponentSerializer;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.util.LegacyComponent;
//import io.th0rgal.oraxen.items.OraxenItems;
import me.clip.placeholderapi.PlaceholderAPI;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.MMOItemsAPI;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.VolatileMMOItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.lone.itemsadder.api.CustomStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ItemSerializer {

        public static ItemStack putData(ItemStack item, String tag, Object object) {
                ItemStack stack = item;
                if (!item.hasItemMeta())
                        return item;
                ItemMeta meta = item.getItemMeta();
                if (object.getClass().getName().equals("java.lang.String")) {
                        meta.getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.STRING,
                                        (String) object);
                } else if (object.getClass().getName().equals("java.lang.Integer")) {
                        meta.getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.INTEGER,
                                        (Integer) object);
                } else if (object.getClass().getName().equals("java.lang.Double")) {
                        meta.getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.DOUBLE,
                                        (Double) object);
                } else if (object.getClass().getName().equals("java.lang.Float")) {
                        meta.getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.FLOAT,
                                        (Float) object);
                } else if (object.getClass().getName().equals("java.lang.Long")) {
                        meta.getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.LONG,
                                        (Long) object);
                } else if (object.getClass().getName().equals("java.lang.Short")) {
                        meta.getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.SHORT,
                                        (Short) object);
                } else if (object.getClass().getName().equals("java.lang.Byte")) {
                        meta.getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.BYTE,
                                        (Byte) object);
                } else if (object.getClass().getName().equals("java.lang.Boolean")) {
                        meta.getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.STRING,
                                        object.toString());
                }
                stack.setItemMeta(meta);
                NBTItem nbtItem = NBTItem.get(stack);
                nbtItem.addTag(new ItemTag(tag, object));

                return nbtItem.toItem();
        }

        public static Boolean containsData(ItemStack item, String tag) {
                return item.getItemMeta().getPersistentDataContainer()
                                .has(new NamespacedKey(MComesToLife.getPlugin(), tag)) || NBTItem.get(item).hasTag(tag);
        }

        public static <T> T getData(ItemStack item, String tag, Class<T> Type) {
                if (Type.getName().equals("java.lang.String")) {
                        return (T) item.getItemMeta().getPersistentDataContainer().get(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.STRING);
                } else if (Type.getName().equals("java.lang.Integer")) {
                        return (T) item.getItemMeta().getPersistentDataContainer().get(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.INTEGER);
                } else if (Type.getName().equals("java.lang.Double")) {
                        return (T) item.getItemMeta().getPersistentDataContainer().get(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.DOUBLE);
                } else if (Type.getName().equals("java.lang.Float")) {
                        return (T) item.getItemMeta().getPersistentDataContainer().get(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.FLOAT);
                } else if (Type.getName().equals("java.lang.Long")) {
                        return (T) item.getItemMeta().getPersistentDataContainer()
                                        .get(new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.LONG);
                } else if (Type.getName().equals("java.lang.Short")) {
                        return (T) item.getItemMeta().getPersistentDataContainer().get(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.SHORT);
                } else if (Type.getName().equals("java.lang.Byte")) {
                        return (T) item.getItemMeta().getPersistentDataContainer()
                                        .get(new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.BYTE);
                } else if (Type.getName().equals("java.lang.Boolean")) {
                        return (T) Boolean.valueOf(item.getItemMeta().getPersistentDataContainer()
                                        .get(new NamespacedKey(MComesToLife.getPlugin(), tag),
                                                        PersistentDataType.STRING));
                }
                NBTItem nbtItem = NBTItem.get(item);
                if (nbtItem.hasTag(tag)) {
                        return (T) nbtItem.get(tag);
                }
                return null;
        }

        public static String write(ItemStack... items) {
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

                        dataOutput.writeInt(items.length);

                        for (ItemStack item : items)
                                dataOutput.writeObject(item);

                        return Base64Coder.encodeLines(outputStream.toByteArray());

                } catch (Exception ignored) {
                        return "";
                }
        }

        public static ItemStack[] read(String source) {
                try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(source));
                                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

                        ItemStack[] items = new ItemStack[dataInput.readInt()];

                        for (int i = 0; i < items.length; i++)
                                items[i] = (ItemStack) dataInput.readObject();

                        return items;
                } catch (Exception ignored) {
                        return emptyInv;
                }
        }

        private static ItemStack[] emptyInv = new ItemStack[1];
        static {
                emptyInv[0] = new ItemStack(Material.AIR);
        }

        @Nullable
        public static ItemStack getFromConfigurationSection(ConfigurationSection section) {

                CustomConfigurationSection configB = new CustomConfigurationSection(section);
                Material a = Material.DIAMOND;
                ItemStack b = null;
                String mat = configB.getString(new String[] { "Material", "MATERIAL", "MATERIAL" },
                                "DIAMOND");

                if (Material.getMaterial(mat) == null) {
                        if (MComesToLife.getEnabledPlugins().isEnabled("ItemsAdder")
                                        || MComesToLife.getEnabledPlugins().isEnabled("ItemsAdders")) {
                                if (CustomStack.getInstance(mat) != null) {
                                        b = CustomStack.getInstance(mat).getItemStack();
                                } else {
                                        a = Material.getMaterial(configB.getString(
                                                        new String[] { "Material", "MATERIAL", "MATERIAL" },
                                                        "DIAMOND"));
                                        b = new ItemStack(a);
                                }
                        }
                        // if (MComesToLife.getEnabledPlugins().isEnabled("Oraxen")
                        // || MComesToLife.getEnabledPlugins().isEnabled("OraxenPlugin")) {
                        // if (OraxenItems.exists(mat)) {
                        // b = OraxenItems.getItemById(mat).build();
                        // } else {
                        // a = Material.getMaterial(configB.getString(
                        // new String[] { "Material", "MATERIAL", "MATERIAL" },
                        // "DIAMOND"));
                        // b = new ItemStack(a);
                        // }
                        // }

                } else {
                        a = Material.getMaterial(configB.getString(new String[] { "Material", "MATERIAL", "MATERIAL" },
                                        "DIAMOND"));
                        b = new ItemStack(a);
                }

                // add enchants from config section "enchants"
                if (section.getConfigurationSection("enchants") != null) {
                        if (a == Material.ENCHANTED_BOOK) {
                                for (String ench : section.getConfigurationSection("enchants").getKeys(false)) {
                                        try {
                                                ((org.bukkit.inventory.meta.EnchantmentStorageMeta) b.getItemMeta())
                                                                .addStoredEnchant(
                                                                                Enchantment.getByName(ench),
                                                                                section.getInt("enchants." + ench),
                                                                                true);
                                        } catch (Exception e) {
                                                if (MComesToLife.isDEBUG()) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }
                        } else {
                                for (String ench : section.getConfigurationSection("enchants").getKeys(false)) {
                                        try {
                                                b.addUnsafeEnchantment(Enchantment.getByName(ench),
                                                                section.getInt("enchants." + ench));
                                        } catch (Exception e) {
                                                if (MComesToLife.isDEBUG()) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }
                        }
                }

                ItemMeta meta = b.getItemMeta();
                meta.setDisplayName(configB.getString(
                                new String[] { "Name", "NAME", "name", "DisplayName", "DISPLAYNAME", "displayname" },
                                "Name"));

                meta.setLore(configB.getStringList(new String[] { "Lore", "LORE", "lore" }, new ArrayList<String>()));
                b.setItemMeta(meta);
                // set stack size
                b.setAmount(configB.getInt(new String[] { "Amount", "AMOUNT", "amount" }, 1));
                // add persistent data containers
                if (section.getConfigurationSection("persistent-data") != null) {
                        for (String key : section.getConfigurationSection("persistent-data").getKeys(false)) {
                                Plugin pluginToKey = Bukkit.getPluginManager().getPlugin(configB.getString(
                                                new String[] { "persistent-data." + key + ".plugin",
                                                                "persistent-data." + key + ".plug" },
                                                MComesToLife.getPlugin().getName())) == null ? MComesToLife.getPlugin()
                                                                : (Plugin) Bukkit.getPluginManager()
                                                                                .getPlugin(configB.getString(
                                                                                                new String[] { "plugin",
                                                                                                                "plug" },
                                                                                                MComesToLife.getPlugin()
                                                                                                                .getName()));
                                b.getItemMeta().getPersistentDataContainer().set(
                                                new NamespacedKey(pluginToKey,
                                                                configB.getString(new String[] {
                                                                                "persistent-data." + key + ".key",
                                                                                "persistent-data." + key + ".id" },
                                                                                "")),
                                                configB.getType(new String[] { "persistent-data." + key + ".type",
                                                                "persistent-data." + key + ".TYPE" }),
                                                configB.get(new String[] { "persistent-data." + key + ".object" },
                                                                null));
                        }
                }

                // NBT manipulation
                NBTItem z = NBTItem.get(b);
                z.addTag(new ItemTag("Unbreakable",
                                configB.getBoolean(new String[] { "Unbreakable", "UNBREAKABLE", "unbreakable" },
                                                false)));
                z.addTag(new ItemTag("HideFlags",
                                configB.getInt(new String[] { "HideFlags", "HIDEFLAGS", "hideflags" }, 0)));
                z.addTag(new ItemTag("HideAttributes",
                                configB.getBoolean(
                                                new String[] { "HideAttributes", "HIDEATTRIBUTES", "hideattributes" },
                                                false)));
                z.addTag(new ItemTag("HideUnbreakable",
                                configB.getBoolean(new String[] { "HideUnbreakable", "HIDEUNBREAKABLE",
                                                "hideunbreakable" }, false)));
                z.addTag(new ItemTag("HideDestroys",
                                configB.getBoolean(new String[] { "HideDestroys", "HIDEDESTROYS", "hidedestroys" },
                                                false)));
                z.addTag(new ItemTag("HidePlacedOn",
                                configB.getBoolean(new String[] { "HidePlacedOn", "HIDEPLACEDON", "hideplacedon" },
                                                false)));
                z.addTag(new ItemTag("HidePotionEffects", configB
                                .getBoolean(new String[] { "HidePotionEffects", "HIDEPOTIONEFFECTS",
                                                "hidepotioneffects" }, false)));
                z.addTag(new ItemTag("HideEnchants",
                                configB.getBoolean(new String[] { "HideEnchants", "HIDEENCHANTS", "hideenchants" },
                                                false)));
                z.addTag(new ItemTag("HideMisc",
                                configB.getBoolean(new String[] { "HideMisc", "HIDEMISC", "hidemisc" }, false)));
                z.addTag(new ItemTag("HideDye",
                                configB.getBoolean(new String[] { "HideDye", "HIDEDYE", "hidedye" }, false)));
                z.addTag(new ItemTag("HideBlockEntityTag", configB
                                .getBoolean(new String[] { "HideBlockEntityTag", "HIDEBLOCKENTITYTAG",
                                                "hideblockentitytag" }, false)));
                z.addTag(new ItemTag("HideCanDestroy",
                                configB.getBoolean(
                                                new String[] { "HideCanDestroy", "HIDECANDESTROY", "hidecandestroy" },
                                                false)));
                z.addTag(new ItemTag("HideCanPlaceOn",
                                configB.getBoolean(
                                                new String[] { "HideCanPlaceOn", "HIDECANPLACEON", "hidecanplaceon" },
                                                false)));
                z.addTag(new ItemTag("HideUnbreakable",
                                configB.getBoolean(new String[] { "HideUnbreakable", "HIDEUNBREAKABLE",
                                                "hideunbreakable" }, false)));
                z.addTag(new ItemTag("CustomModelData",
                                configB.getInteger(new String[] { "CMD", "CustomModelData", "custommodeldata" }, 0)));
                z.addTag(new ItemTag("Damage", configB.getInteger(new String[] { "Damage", "DAMAGE", "damage" }, 0)));

                // if(configB.contains(new String[]{"tags", "TAGS", "Tags"})) {
                // List<String> tags = configB.getStringList(new String[]{"tags", "TAGS",
                // "Tags"}, new ArrayList<String>());
                // tags.forEach(tag->{
                // LineConfi
                // });
                // }
                return z.toItem();
        }

        public static ItemStack generateItem(ItemStack stack) {
                ItemStack clone = stack.clone();
                NBTItem z = NBTItem.get(clone);
                if (clone.getItemMeta().hasDisplayName()) {
                        z.setDisplayNameComponent(LegacyComponent.parse(clone.getItemMeta().getDisplayName()));
                }
                if (clone.getItemMeta().hasLore()) {
                        List<Component> lore = new ArrayList<>();
                        clone.getItemMeta().getLore().forEach(line -> {
                                lore.add(LegacyComponent.parse(line));
                        });
                        z.setLoreComponents(lore);
                }
                return z.toItem();
        }

        public static ItemStack generateItem(ItemStack stack, Player p) {
                if (!stack.hasItemMeta())
                        return stack;
                ItemStack clone = stack.clone();
                NBTItem z = NBTItem.get(clone);
                if (clone.getItemMeta().hasDisplayName()) {

                        if (MComesToLife.getEnabledPlugins().isEnabled("PlaceholderAPI")) {
                                z.setDisplayNameComponent(LegacyComponent.parse(PlaceholderAPI.setPlaceholders(p,
                                                MessageUtils.StringParsedPlaceholders(p,
                                                                new Message(clone.getItemMeta()
                                                                                .getDisplayName()),
                                                                DataMethods.getlastClickedEntity(
                                                                                p)))));
                        } else {
                                z.setDisplayNameComponent(LegacyComponent.parse(MessageUtils.StringParsedPlaceholders(p,
                                                new Message(clone.getItemMeta().getDisplayName()),
                                                DataMethods.getlastClickedEntity(p))));
                        }
                }
                if (clone.getItemMeta().hasLore()) {
                        List<Component> lore = new ArrayList<>();
                        clone.getItemMeta().getLore().forEach(line -> {
                                String a = MessageUtils.StringParsedPlaceholders(p, new Message(line),
                                                DataMethods.getlastClickedEntity(p));
                                if (MComesToLife.getEnabledPlugins().isEnabled("PlaceholderAPI")) {
                                        a = PlaceholderAPI.setPlaceholders(p, MessageUtils.StringParsedPlaceholders(p,
                                                        new Message(line), DataMethods.getlastClickedEntity(p)));
                                }
                                lore.add(LegacyComponent.parse(a));
                        });
                        z.setLoreComponents(lore);
                }

                return z.toItem();
        }

        @NotNull
        public static String getType(@Nullable ItemStack stack) {
                if (stack == null)
                        return "NULL";
                String type = "NULL";
                if (MComesToLife.getEnabledPlugins().isEnabled("ItemsAdder")
                                || MComesToLife.getEnabledPlugins().isEnabled("ItemsAdders")) {
                        if (CustomStack.byItemStack(stack) != null) {
                                return CustomStack.byItemStack(stack).getNamespacedID().toUpperCase();
                        }
                }
                // if (MComesToLife.getEnabledPlugins().isEnabled("Oraxen")
                // || MComesToLife.getEnabledPlugins().isEnabled("OraxenPlugin")) {
                // if (OraxenItems.exists(stack)) {
                // return OraxenItems.getIdByItem(stack).toUpperCase();
                // }
                // }
                if (stack.hasItemMeta()) {
                        if (stack.getItemMeta().hasCustomModelData()) {
                                if (stack.getItemMeta().getCustomModelData() > 0) {
                                        return stack.getType().toString().toUpperCase() + "_"
                                                        + stack.getItemMeta().getCustomModelData();
                                }
                        }
                }

                return stack.getType().toString().toUpperCase();
        }

        public static int getEnchantmentLevel(ItemStack stack, String enchant) {
                for (Enchantment Enchant : stack.getEnchantments().keySet()) {

                        if (Enchant.toString().contains(enchant.toUpperCase())) {
                                return stack.getEnchantmentLevel(Enchant);
                        }
                }
                return 0;
        }

        public static float getDamage(ItemStack stack) {
                if (stack == null)
                        return 0;
                if (stack.getType().isAir())
                        return 0;
                float a = 0f;
                if (MComesToLife.getEnabledPlugins().isEnabled("MMOItems")) {
                        if (NBTItem.get(stack).hasTag("MMOITEMS_ITEM_ID")) {
                                VolatileMMOItem mmoitem = new VolatileMMOItem(NBTItem.get(stack));
                                if (NumberUtils.isCreatable(mmoitem.getData(ItemStats.ATTACK_DAMAGE).toString())) {
                                        a = Float.parseFloat(mmoitem.getData(ItemStats.ATTACK_DAMAGE).toString());
                                }
                        }
                }
                if (NBTItem.get(stack).hasTag("MMOITEMS_ATTACK_DAMAGE")) {
                        a = (float) NBTItem.get(stack).getInteger("MMOITEMS_ATTACK_DAMAGE");
                        return a;
                }
                if (stack.hasItemMeta()) {
                        for (AttributeModifier b : stack.getItemMeta()
                                        .getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)) {
                                a += (float) b.getAmount();
                        }
                } else {
                        for (Attribute b : stack.getType().getDefaultAttributeModifiers(EquipmentSlot.HAND).asMap()
                                        .keySet()) {
                                if (b.equals(Attribute.GENERIC_ATTACK_DAMAGE)) {
                                        a += (float) stack.getType().getDefaultAttributeModifiers(EquipmentSlot.HAND)
                                                        .asMap().get(b).iterator().next().getAmount();
                                }
                        }
                }

                // apply sharpness formula
                if (getEnchantmentLevel(stack, SerializedEnchantment.SHARPNESS.getEnchantmentName()) > 0) {
                        a += (float) (getEnchantmentLevel(stack, SerializedEnchantment.SHARPNESS.getEnchantmentName())
                                        * 1.25);
                }

                return a;
        }

        public static float getDamage(ItemStack stack, net.minecraft.world.entity.LivingEntity target) {

                float a = getDamage(stack);

                if (a == 0f) {
                        return 3f;
                }

                if (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.ZOMBIE)) {
                        a += (float) (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName())
                                        * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.SKELETON)) {
                        a += (float) (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName())
                                        * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.WITHER_SKELETON)) {
                        a += (float) (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName())
                                        * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.ZOMBIE_VILLAGER)) {
                        a += (float) (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName())
                                        * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.HUSK)) {
                        a += (float) (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName())
                                        * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.DROWNED)) {
                        a += (float) (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName())
                                        * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.ZOMBIFIED_PIGLIN)) {
                        a += (float) (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName())
                                        * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.PHANTOM)) {
                        a += (float) (getEnchantmentLevel(stack, SerializedEnchantment.SMITE.getEnchantmentName())
                                        * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.BANE_OF_ARTHROPODS.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.SPIDER)) {
                        a += (float) (getEnchantmentLevel(stack,
                                        SerializedEnchantment.BANE_OF_ARTHROPODS.getEnchantmentName()) * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.BANE_OF_ARTHROPODS.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.CAVE_SPIDER)) {
                        a += (float) (getEnchantmentLevel(stack,
                                        SerializedEnchantment.BANE_OF_ARTHROPODS.getEnchantmentName()) * 1.25);
                }
                if (getEnchantmentLevel(stack, SerializedEnchantment.BANE_OF_ARTHROPODS.getEnchantmentName()) > 0
                                && target.getType().equals(EntityType.SILVERFISH)) {
                        a += (float) (getEnchantmentLevel(stack,
                                        SerializedEnchantment.BANE_OF_ARTHROPODS.getEnchantmentName()) * 1.25);
                }

                return a;
        }

        public enum SerializedEnchantment {
                PROTECTION(Enchantment.PROTECTION_ENVIRONMENTAL),
                FIRE_PROTECTION(Enchantment.PROTECTION_FIRE),
                FEATHER_FALLING(Enchantment.PROTECTION_FALL),
                BLAST_PROTECTION(Enchantment.PROTECTION_EXPLOSIONS),
                PROJECTILE_PROTECTION(Enchantment.PROTECTION_PROJECTILE),
                RESPIRATION(Enchantment.OXYGEN),
                AQUA_AFFINITY(Enchantment.WATER_WORKER),
                THORNS(Enchantment.THORNS),
                DEPTH_STRIDER(Enchantment.DEPTH_STRIDER),
                FROST_WALKER(Enchantment.FROST_WALKER),
                BINDING_CURSE(Enchantment.BINDING_CURSE),
                SOUL_SPEED(Enchantment.SOUL_SPEED),
                SHARPNESS(Enchantment.DAMAGE_ALL),
                SMITE(Enchantment.DAMAGE_UNDEAD),
                BANE_OF_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS),
                KNOCKBACK(Enchantment.KNOCKBACK),
                FIRE_ASPECT(Enchantment.FIRE_ASPECT),
                LOOTING(Enchantment.LOOT_BONUS_MOBS),
                SWEEPING_EDGE(Enchantment.SWEEPING_EDGE),
                EFFICIENCY(Enchantment.DIG_SPEED),
                SILK_TOUCH(Enchantment.SILK_TOUCH),
                UNBREAKING(Enchantment.DURABILITY),
                FORTUNE(Enchantment.LOOT_BONUS_BLOCKS),
                POWER(Enchantment.ARROW_DAMAGE),
                PUNCH(Enchantment.ARROW_KNOCKBACK),
                FLAME(Enchantment.ARROW_FIRE),
                INFINITY(Enchantment.ARROW_INFINITE),
                LUCK_OF_THE_SEA(Enchantment.LUCK),
                LURE(Enchantment.LURE),
                LOYALTY(Enchantment.LOYALTY),
                IMPALING(Enchantment.IMPALING),
                RIPTIDE(Enchantment.RIPTIDE),
                CHANNELING(Enchantment.CHANNELING),
                MULTISHOT(Enchantment.MULTISHOT),
                QUICK_CHARGE(Enchantment.QUICK_CHARGE),
                PIERCING(Enchantment.PIERCING),
                MENDING(Enchantment.MENDING),
                VANISHING_CURSE(Enchantment.VANISHING_CURSE);

                private Enchantment nmsEnchantment;

                private SerializedEnchantment(Enchantment enchantment) {
                        this.nmsEnchantment = enchantment;
                }

                public Enchantment getEnchantment() {
                        return this.nmsEnchantment;
                }

                public String getEnchantmentName() {
                        return this.nmsEnchantment.toString().toLowerCase();
                }
        }

        public static Long getCooldown(ItemStack stack) {
                Long returned = 20L;
                if (stack.getType().toString().toLowerCase().contains("bow"))
                        returned = 15L;
                if (stack.getType().toString().toLowerCase().contains("bow") && getEnchantmentLevel(stack,
                                SerializedEnchantment.QUICK_CHARGE.getEnchantmentName()) > 0) {
                        returned -= (getEnchantmentLevel(stack,
                                        SerializedEnchantment.QUICK_CHARGE.getEnchantmentName()));
                }

                return returned;
        }

        public static Long getCooldown(ItemStack stack, Entity mob) {
                Long returned = 30L;
                if (stack.getType().toString().toLowerCase().contains("bow"))
                        returned = 15L;
                if (stack.getType().toString().toLowerCase().contains("bow") && getEnchantmentLevel(stack,
                                SerializedEnchantment.QUICK_CHARGE.getEnchantmentName()) > 0) {
                        returned -= (getEnchantmentLevel(stack,
                                        SerializedEnchantment.QUICK_CHARGE.getEnchantmentName()));
                }
                if (mob.isInWater() && getEnchantmentLevel(stack,
                                SerializedEnchantment.AQUA_AFFINITY.getEnchantmentName()) > 0) {
                        returned -= (getEnchantmentLevel(stack,
                                        SerializedEnchantment.AQUA_AFFINITY.getEnchantmentName()));
                }

                return returned;
        }
}