package dev.arubik.mctl.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import io.lumine.mythic.lib.adventure.text.Component;
import io.lumine.mythic.lib.adventure.text.TextComponent;
import io.lumine.mythic.lib.adventure.text.serializer.legacy.LegacyComponentSerializer;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.util.LegacyComponent;
import io.th0rgal.oraxen.items.OraxenItems;
import me.clip.placeholderapi.PlaceholderAPI;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.holders.Message;
import dev.lone.itemsadder.api.CustomStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ItemSerializer {

        public static ItemStack putData(ItemStack item, String tag, Object object) {
                ItemStack stack = item;
                if (object.getClass().getName().equals("java.lang.String")) {
                        stack.getItemMeta().getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.STRING,
                                        (String) object);
                } else if (object.getClass().getName().equals("java.lang.Integer")) {
                        stack.getItemMeta().getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.INTEGER,
                                        (Integer) object);
                } else if (object.getClass().getName().equals("java.lang.Double")) {
                        stack.getItemMeta().getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.DOUBLE,
                                        (Double) object);
                } else if (object.getClass().getName().equals("java.lang.Float")) {
                        stack.getItemMeta().getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.FLOAT,
                                        (Float) object);
                } else if (object.getClass().getName().equals("java.lang.Long")) {
                        stack.getItemMeta().getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.LONG,
                                        (Long) object);
                } else if (object.getClass().getName().equals("java.lang.Short")) {
                        stack.getItemMeta().getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.SHORT,
                                        (Short) object);
                } else if (object.getClass().getName().equals("java.lang.Byte")) {
                        stack.getItemMeta().getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.BYTE,
                                        (Byte) object);
                } else if (object.getClass().getName().equals("java.lang.Boolean")) {
                        stack.getItemMeta().getPersistentDataContainer().set(
                                        new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.BYTE,
                                        (Byte) object);
                }

                return stack;
        }

        public static Boolean containsData(ItemStack item, String tag) {
                return item.getItemMeta().getPersistentDataContainer()
                                .has(new NamespacedKey(MComesToLife.getPlugin(), tag));
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
                        return (T) item.getItemMeta().getPersistentDataContainer()
                                        .get(new NamespacedKey(MComesToLife.getPlugin(), tag), PersistentDataType.BYTE);
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
                        return new ItemStack[0];
                }
        }

        @Nullable
        public static ItemStack getFromConfigurationSection(ConfigurationSection section) {
                CustomConfigurationSection configB = new CustomConfigurationSection(section);
                try {
                        Material.valueOf(configB.getString(new String[] { "Material", "MATERIAL", "MATERIAL" },
                                        "DIAMOND"));
                } catch (IllegalArgumentException e) {
                        return null;
                }

                Material a = Material
                                .valueOf(configB.getString(new String[] { "Material", "MATERIAL", "MATERIAL" },
                                                "DIAMOND"));
                ItemStack b = new ItemStack(a);

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
                                        }
                                }
                        } else {
                                for (String ench : section.getConfigurationSection("enchants").getKeys(false)) {
                                        try {
                                                b.addUnsafeEnchantment(Enchantment.getByName(ench),
                                                                section.getInt("enchants." + ench));
                                        } catch (Exception e) {
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
                                                                "persistent-data." + key + "plug" },
                                                MComesToLife.getPlugin().getName())) == null ? MComesToLife.plugin
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

        public static ItemStack generateItem(ItemStack stack, Player p) {
                ItemStack clone = stack.clone();
                NBTItem z = NBTItem.get(clone);
                if (clone.getItemMeta().hasDisplayName()) {

                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                                z.setDisplayNameComponent(LegacyComponent.parse(PlaceholderAPI.setPlaceholders(p,
                                                messageUtils.StringParsedPlaceholders(p,
                                                                new Message(clone.getItemMeta()
                                                                                .getDisplayName()),
                                                                MComesToLife.getlastClickedEntity(
                                                                                p)))));
                        } else {
                                z.setDisplayNameComponent(LegacyComponent.parse(messageUtils.StringParsedPlaceholders(p,
                                                new Message(clone.getItemMeta().getDisplayName()),
                                                MComesToLife.getlastClickedEntity(p))));
                        }
                }
                if (clone.getItemMeta().hasLore()) {
                        List<Component> lore = new ArrayList<>();
                        clone.getItemMeta().getLore().forEach(line -> {
                                String a = messageUtils.StringParsedPlaceholders(p, new Message(line),
                                                MComesToLife.getlastClickedEntity(p));
                                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                                        a = PlaceholderAPI.setPlaceholders(p, messageUtils.StringParsedPlaceholders(p,
                                                        new Message(line), MComesToLife.getlastClickedEntity(p)));
                                }
                                lore.add(LegacyComponent.parse(a));
                        });
                        z.setLoreComponents(lore);
                }

                return z.toItem();
        }

        @NotNull
        public static String getType(ItemStack stack) {
                if (MComesToLife.getEnabledPlugins().isEnabled("ItemsAdder")
                                || MComesToLife.getEnabledPlugins().isEnabled("ItemsAdders")) {
                        if (CustomStack.byItemStack(stack) != null) {
                                return CustomStack.byItemStack(stack).getNamespacedID().toUpperCase();
                        }
                }
                if (MComesToLife.getEnabledPlugins().isEnabled("Oraxen")
                                || MComesToLife.getEnabledPlugins().isEnabled("OraxenPlugin")) {
                        if (OraxenItems.getIdByItem(stack) != null) {
                                return OraxenItems.getIdByItem(stack).toUpperCase();
                        }
                }
                if (stack.getItemMeta().hasCustomModelData()) {
                        if (stack.getItemMeta().getCustomModelData() > 0) {
                                return stack.getType().toString().toUpperCase() + "_"
                                                + stack.getItemMeta().getCustomModelData();
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
}