package dev.arubik.mctl.holders.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.citizensnpcs.api.CitizensAPI;
import net.skinsrestorer.api.SkinVariant;
import net.skinsrestorer.api.exception.SkinRequestException;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.Works;
import dev.arubik.mctl.enums.mood;
import dev.arubik.mctl.enums.sex;
import dev.arubik.mctl.enums.trait;
import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.holders.timers;
import dev.arubik.mctl.utils.FileConfiguration;
import dev.arubik.mctl.utils.fileUtils;
import dev.arubik.mctl.utils.messageUtils;

public class DataMethods {

    public static CustomVillager getVillager(LivingEntity a) {
        CustomVillager b = new CustomVillager(a);
        b.loadVillager(false);
        return b;
    }

    public static VillagerInventoryHolder getVillagerInventoryHolder(LivingEntity a) {
        VillagerInventoryHolder b = new VillagerInventoryHolder(a);
        b.loadInventory();
        return b;
    }

    public static mood getMood(LivingEntity entity) {
        return mood.valueOf((String) Optional
                .ofNullable(
                        fileUtils.getFileConfiguration("data.yml").getConfig()
                                .get(DataMethods.path(entity) + ".mood", "NEUTRAL"))
                .orElse("NEUTRAL"));
    }

    public static int rand(int min, int max) {
        return (new Random()).nextInt(max - min + 1) + min;
    }

    public static Double Doublerand(int min, int max) {
        return (new Random()).nextDouble(max - min + 1) + min;
    }

    public static Boolean avaliable(Entity e) {

        if (MComesToLife.getEnabledPlugins().isEnabled("MythicMobs")) {
            if (MythicBukkit.inst().getMobManager().isActiveMob(e.getUniqueId())) {
                return false;
            }
        }
        if (MComesToLife.getEnabledPlugins().isEnabled("Citizens")) {
            if (CitizensAPI.getNPCRegistry().isNPC(e)) {
                return false;
            }
        }

        if (MComesToLife.getEnabledPlugins().isEnabled("ShopKeepers")) {
            if (ShopkeepersAPI.getShopkeeperRegistry().isShopkeeper(e)) {
                return false;
            }
        }
        return true;
    }

    public static CustomVillager loadBabyVillager(Boolean reApplySkin, LivingEntity father, LivingEntity mother) {
        Villager vil = (Villager) mother.getLocation().getWorld().spawnEntity(mother.getLocation().add(0, 1, 0),
                EntityType.VILLAGER);
        vil.setAge(0);
        CustomVillager cvil = new CustomVillager(vil);
        cvil.putData("father", father.getUniqueId().toString());
        cvil.putData("mother", mother.getUniqueId().toString());
        cvil.putData("age", 0);
        cvil.setupVillager();
        cvil.addLikes(100, father);
        cvil.addLikes(100, mother);
        cvil.save();
        return cvil;
    }

    public static void addBaby(LivingEntity baby, LivingEntity parent) {
        if (parent instanceof Player) {
            List<String> sonsList = (List<String>) DataMethods.retrivePlayerData((Player) parent).getOrDefault("sons",
                    new ArrayList<String>());
            sonsList.add(baby.getUniqueId().toString());
            DataMethods.setData("sons", sonsList, parent);
        } else {
            List<String> sonsList = (List<String>) DataMethods.retriveData((Player) parent).getOrDefault("sons",
                    new ArrayList<String>());
            sonsList.add(baby.getUniqueId().toString());
            DataMethods.setData("sons", sonsList, parent);
        }
    }

    public static void removeBaby(LivingEntity baby, LivingEntity parent) {
        if (parent instanceof Player) {
            List<String> sonsList = (List<String>) DataMethods.retrivePlayerData((Player) parent).getOrDefault("sons",
                    new ArrayList<String>());
            sonsList.remove(baby.getUniqueId().toString());
            DataMethods.setData("sons", sonsList, parent);
        } else {
            List<String> sonsList = (List<String>) DataMethods.retriveData((Player) parent).getOrDefault("sons",
                    new ArrayList<String>());
            sonsList.remove(baby.getUniqueId().toString());
            DataMethods.setData("sons", sonsList, parent);
        }
    }

    public static Boolean isSon(LivingEntity baby, LivingEntity parent) {
        if (parent instanceof Player) {
            List<String> sonsList = (List<String>) DataMethods.retrivePlayerData((Player) parent).getOrDefault("sons",
                    new ArrayList<String>());
            if (sonsList.contains(baby.getUniqueId().toString())) {
                return true;
            }
        } else {
            List<String> sonsList = (List<String>) DataMethods.retriveData((Player) parent).getOrDefault("sons",
                    new ArrayList<String>());
            sonsList.remove(baby.getUniqueId().toString());
            if (sonsList.contains(baby.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getSons(LivingEntity parent) {
        if (parent instanceof Player) {
            List<String> sonsList = (List<String>) DataMethods.retrivePlayerData((Player) parent).getOrDefault("sons",
                    new ArrayList<String>());
            return sonsList;
        } else {
            List<String> sonsList = (List<String>) DataMethods.retriveData(parent).getOrDefault("sons",
                    new ArrayList<String>());
            return sonsList;
        }
    }

    public static boolean isVillager(Entity e) {
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        if (file.getConfig().getString(DataMethods.path((LivingEntity) e) + ".name") != "any")
            return true;
        return false;
    }

    public static Boolean setSex(LivingEntity e, sex s) {
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        if (timers.entEnabled(e)) {
            file.getConfig().set(path(e) + "." + "sex", s.toString().toLowerCase());
            fileUtils.saveFile(file.getConfig(), "data.yml");
            return true;
        }
        if (e instanceof Player || e instanceof OfflinePlayer) {
            file.getConfig().set(playerPath((OfflinePlayer) e) + "." + "sex", s.toString().toLowerCase());
            fileUtils.saveFile(file.getConfig(), "data.yml");
            return true;
        }
        return false;
    }

    public static sex getSex(LivingEntity e) {
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        if (timers.entEnabled(e)) {
            return sex.valueOf(DataMethods.retriveData(e).getOrDefault("sex", "male").toString());
        }
        if (e instanceof Player || e instanceof OfflinePlayer) {
            return sex.valueOf(DataMethods.retrivePlayerData((Player) e).getOrDefault("sex", "male").toString());
        }
        return null;
    }

    public static HashMap<String, Object> retriveData(LivingEntity e) {
        HashMap<String, Object> mapData = new HashMap<String, Object>();
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        if (file.getConfig().contains(path(e))) {
            for (String key : file.getConfig().getConfigurationSection(pathEnt(e)).getKeys(false)) {
                mapData.put(key, file.getConfig().get(path(e) + "." + key));
            }
        } else {
            mapData.put("name", "any");
        }
        for (String key : file.getConfig().getConfigurationSection(pathEnt(e)).getKeys(false)) {
            mapData.put(key, file.getConfig().get(pathEnt(e) + "." + key));
        }
        return mapData;
    }

    public static HashMap<String, Object> retriveData(String e) {
        HashMap<String, Object> mapData = new HashMap<String, Object>();
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        if (file.getConfig().contains(path(e))) {
            for (String key : file.getConfig().getConfigurationSection(pathEnt(e)).getKeys(false)) {
                mapData.put(key, file.getConfig().get(path(e) + "." + key));
            }
        } else {
            mapData.put("name", "any");
        }
        for (String key : file.getConfig().getConfigurationSection(pathEnt(e)).getKeys(false)) {
            mapData.put(key, file.getConfig().get(pathEnt(e) + "." + key));
        }
        return mapData;
    }

    public static HashMap<String, Object> retrivePlayerData(OfflinePlayer player) {
        HashMap<String, Object> mapData = new HashMap<String, Object>();
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        if (file.getConfig().contains(playerPath(player))) {
            for (String key : file.getConfig().getConfigurationSection(playerPath(player)).getKeys(false)) {
                mapData.put(key, file.getConfig().get(playerPath(player) + "." + key));
            }
        } else {
            mapData.put("name", "any");
        }
        return mapData;
    }

    public static Boolean marry(LivingEntity a, LivingEntity b, int extraLikes) {

        CustomVillager vil = null;
        Player p = null;

        if (DataMethods.isCustom(a)) {
            vil = new CustomVillager(a);
            p = (Player) b;
        } else if (DataMethods.isCustom(b)) {
            vil = new CustomVillager(b);
            p = (Player) a;
        }

        if (MComesToLife.config.getInt("config.marry-likes", 0) < vil.getLikes(p).orElse(0) + extraLikes) {
            Message msg = new Message(mood.getText("marry-no", ""));
            msg.formatPlayerSex(DataMethods.getSex(p));
            msg.formatSex(DataMethods.getSex(a));
            messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) a), msg, vil);
            return false;
        }

        if (vil.getSpouse() != null) {
            if (p.getUniqueId().toString().equalsIgnoreCase(vil.getSpouse())) {
                Message msg = new Message(MComesToLife.getMessages().getLang("cmd.marry.alredy-spouse",
                        "<prefix><gray>Ya estas casad<player_sex_endchar> con <villager_name></gray>"));
                msg.formatPlayerSex(DataMethods.getSex(p));
                msg.formatSex(DataMethods.getSex(a));
                messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) a), msg, vil);
                return false;
            }
            Message msg = new Message(mood.getText("marry-alredy", ""));
            msg.formatPlayerSex(DataMethods.getSex(p));
            msg.formatSex(DataMethods.getSex(a));
            messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) a), msg, vil);
            return false;
        }

        if (DataMethods.getSpouse(p).isPresent()) {
            Message msg = new Message(mood.getText("marry-cant", ""));
            msg.formatPlayerSex(DataMethods.getSex(p));
            msg.formatSex(DataMethods.getSex(a));
            messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) a), msg, vil);
            return false;
        }

        if (DataMethods.getFamily("", p, vil.villager).equalsIgnoreCase("child")) {
            Message msg = new Message(mood.getText("marry-cant-son", ""));
            msg.formatPlayerSex(DataMethods.getSex(p));
            msg.formatSex(DataMethods.getSex(a));
            messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) a), msg, vil);
            return false;
        }

        DataMethods.setData("spouse", a.getUniqueId().toString(), b);
        DataMethods.setData("spouse", b.getUniqueId().toString(), a);

        Message msg = new Message(mood.getText("marry-yes", ""));
        msg.formatPlayerSex(DataMethods.getSex(p));
        msg.formatSex(DataMethods.getSex(a));
        messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) a), msg, vil);
        return true;
    }

    public static Optional<LivingEntity> getSpouse(LivingEntity e) {
        Optional<LivingEntity> opt = Optional.empty();
        if (DataMethods.getRelationMap(e).get("spouse") != "any") {
            if (Bukkit.getServer()
                    .getEntity(UUID.fromString(DataMethods.getRelationMap(e).get("spouse").toString())) != null) {
                opt = Optional.ofNullable((LivingEntity) Bukkit.getServer()
                        .getEntity(UUID.fromString(DataMethods.getRelationMap(e).get("spouse").toString())));
            } else {
                for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                    if (p.getUniqueId().toString()
                            .equalsIgnoreCase(DataMethods.getRelationMap(e).get("spouse").toString())) {
                        opt = Optional.ofNullable((LivingEntity) p);
                        break;
                    }
                }
            }
        }
        return opt;
    }

    public static String getRealName(String name) {
        return name
                .replace(MComesToLife.getNames().getLang().getString("names.prefix", ""), "")
                .replace(MComesToLife.getNames().getLang().getString("names.suffix", ""), "");
    }

    public static String getSonNames(LivingEntity v) {
        String a = "";
        List<String> villagers = new ArrayList<String>();
        if (getSons(v).isEmpty()) {
            return MComesToLife.getMessages().getLang("cmd.no-sons", "Dont have sons");
        }
        getSons(v).forEach(uuid -> {
            if (isCustom(uuid)) {
                villagers.add(getRealName((String) retriveData(uuid).get("name")));
            }
        });
        if (villagers.size() <= 1) {
            if (villagers.get(0) != null) {
                return villagers.get(0);
            } else {
                return MComesToLife.getMessages().getLang("cmd.no-sons", "Dont have sons");
            }
        } else {
            return String.join(", ", villagers);
        }
    }

    public static HashMap<String, Object> getRelationMap(LivingEntity v) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (timers.entEnabled(v)) {
            map.put("mother", DataMethods.retriveData(v).getOrDefault("mother", "any"));
            map.put("father", DataMethods.retriveData(v).getOrDefault("father", "any"));
            map.put("spouse", DataMethods.retriveData(v).getOrDefault("spouse", "any"));
            for (String keys : map.keySet()) {
                if (keys.startsWith("son")) {
                    map.put(keys, DataMethods.retriveData(v).getOrDefault(keys, "any"));
                }
            }
        } else if (v instanceof Player) {
            map.put("mother", DataMethods.retrivePlayerData((OfflinePlayer) v).getOrDefault("mother", "any"));
            map.put("father", DataMethods.retrivePlayerData((OfflinePlayer) v).getOrDefault("father", "any"));
            map.put("spouse", DataMethods.retrivePlayerData((OfflinePlayer) v).getOrDefault("spouse", "any"));
            for (String keys : map.keySet()) {
                if (keys.startsWith("son")) {
                    map.put(keys, DataMethods.retrivePlayerData((OfflinePlayer) v).getOrDefault(keys, "any"));
                }
            }
        }
        return map;
    }

    public static String getFamily(String type, Player p, LivingEntity v) {
        String message = "";
        try {
            try {
                if (DataMethods.isSon(v, p))
                    return "Child";
            } catch (Exception exception) {
            }

            try {
                if (DataMethods.retrivePlayerData(p).get("spouse").toString()
                        .equalsIgnoreCase(v.getUniqueId().toString()))
                    return "Spouse";
            } catch (Exception exception) {
            }
        } catch (Exception exception) {
        }
        return message;
    }

    public static void setData(String key, @Nullable Object value, LivingEntity e) {
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        if (e instanceof Player || e instanceof OfflinePlayer) {
            file.getConfig().set(DataMethods.playerPath((OfflinePlayer) e) + "." + key, value);
            fileUtils.saveFile(file.getConfig(), "data.yml");
            return;
        }
        file.getConfig().set(DataMethods.path(e) + "." + key, value);
        fileUtils.saveFile(file.getConfig(), "data.yml");
    }

    public static String path(LivingEntity entity) {
        return "villagers." + entity.getUniqueId().toString();
    }

    public static String path(String entity) {
        return "villagers." + entity;
    }

    public static String playerPath(OfflinePlayer entity) {
        return "players." + entity.getUniqueId().toString();
    }

    public static String pathEnt(LivingEntity e) {
        return "villagers." + e.getUniqueId().toString();
    }

    public static String pathEnt(String e) {
        return "villagers." + e;
    }

    public static String pathEnt(OfflinePlayer e) {
        return "players." + e.getUniqueId().toString();
    }

    public static Boolean isCustom(Entity e) {
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        if (e instanceof LivingEntity) {
            return file.getConfig().contains(DataMethods.pathEnt((LivingEntity) e));
        } else {
            return false;
        }
    }

    public static Boolean isCustom(String e) {
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        return file.getConfig().contains(DataMethods.pathEnt(e));
    }

}
