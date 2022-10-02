package dev.arubik.mctl.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.ItemStack;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.FileConfiguration;
import dev.arubik.mctl.utils.ItemSerializer;
import dev.arubik.mctl.utils.fileUtils;
import lombok.Getter;

public class CustomEntity {
    protected final static Random random = new Random();

    public CustomEntity(LivingEntity v) {
        villager = v;
    }

    public LivingEntity villager;

    @Getter
    protected HashMap<String, Object> data = new HashMap<String, Object>();

    protected HashMap<String, Object> tempData = new HashMap<String, Object>();

    public void setData(String key, Object value) {
        this.putData(key, value);
    }

    public void putData(String key, Object value) {
        data.put(key, value);
        save();
    }

    public void putItems(String key, ItemStack... items) {
        data.put(key, ItemSerializer.write(items));
        save();
    }

    public ItemStack[] getItems(String key) {
        return ItemSerializer.read((String) data.get(key));
    }

    public void putTempData(String key, Object value) {
        tempData.put(key, value);
    }

    public <T> T getData(String name, T type) {
        return (T) data.get(name);
    }

    public Boolean hasData(String name) {
        return data.containsKey(name);
    }

    public void save() {
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        for (String key : data.keySet()) {
            file.getConfig().set(path() + "." + key, data.get(key));
        }
        fileUtils.saveFile(file.getConfig(), "data.yml");
    };

    public void load() {
        FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
        for (String key : file.getConfig().getConfigurationSection(path()).getKeys(false)) {
            if (key.equalsIgnoreCase("tittle")) {
                data.put(key, file.getConfig().get(path() + "." + key));
                data.put("tittle", MComesToLife.getProffesions().getLang("prefix", "")
                        + MComesToLife.getProffesions().getLang((String) file.getConfig().get(path() + "." + "type"),
                                file.getConfig().getString(path() + "." + key).toLowerCase().replace("_", " ")));
            } else {

                data.put(key, file.getConfig().get(path() + "." + key));
            }
        }
    };

    public String playerPath() {
        return "players." + villager.getUniqueId().toString();
    }

    public String path() {
        return "villagers." + villager.getUniqueId().toString();
    };

    public void removeData() {
        Bukkit.getScheduler().runTaskLater(MComesToLife.plugin, () -> {
            FileConfiguration file = fileUtils.getFileConfiguration("data.yml");
            file.getConfig().set(path(), null);
            fileUtils.saveFile(file.getConfig(), "data.yml");
        }, 2L);
    }

    public void dropItems(ItemStack... stack) {
        for (ItemStack a : stack) {
            villager.getWorld().dropItem(villager.getLocation().add(0, 1, 0), a);
        }
    }

    public Location getLocation() {
        return villager.getLocation();
    }

    @Nullable
    public String getSpouse() {
        if (DataMethods.getRelationMap(villager).get("spouse") != null) {
            return (String) DataMethods.getRelationMap(villager).get("spouse");
        }
        return null;
    }

    public Mob getMob() {
        return (Mob) villager;
    }

    public Creature getCreature() {
        return (Creature) villager;
    }
}
