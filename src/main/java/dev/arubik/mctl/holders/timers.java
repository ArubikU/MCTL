package dev.arubik.mctl.holders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.Sex;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.FileUtils;
import dev.arubik.mctl.utils.MessageUtils;

public class Timers {

    public static void startTimers() {

        Timers.slowTimer();
        Timers.longTimer();
    }

    public static Boolean entEnabled(Entity e) {
        return Optional
                .ofNullable(
                        FileUtils.getFileConfiguration("config.yml").getConfig().getStringList("config.villager-mobs"))
                .orElse(new ArrayList<String>()).contains(e.getType().toString());
    }

    public static Boolean entEnabled(String e) {
        return Optional
                .ofNullable(
                        FileUtils.getFileConfiguration("config.yml").getConfig().getStringList("config.villager-mobs"))
                .orElse(new ArrayList<String>()).contains(e);
    }

    public static Player getPlayerByUuid(String uuid) {
        for (Player p : MComesToLife.getPlugin().getServer().getOnlinePlayers()) {
            if (p.getUniqueId().toString().equals(uuid))
                return p;
        }
        return null;
    }

    public static boolean isBaby(LivingEntity v) {
        if (v instanceof Villager
                && (!((Villager) v).isAdult() || !((Villager) v).canBreed() || v.getEyeHeight() < 1.0D))
            return true;
        return false;
    }

    public static void slowTimer() {
        HashMap<String, LivingEntity> vMap = new HashMap<>();
        for (World w : MComesToLife.getPlugin().getServer().getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e instanceof LivingEntity) {
                    if (!DataMethods.isCustom(e)) {
                        return;
                    }
                    CustomVillager vil = new CustomVillager((LivingEntity) e);
                    vil.loadVillager(false);
                    if (!isBaby((LivingEntity) e)) {
                        HashMap<String, Object> tmp = vil.getData();
                        Optional.ofNullable(getPlayerByUuid((String) tmp.get("father"))).ifPresent(pl -> {
                            MessageUtils.MessageParsedPlaceholders((CommandSender) pl, new Message(
                                    MComesToLife.getMessages().getLang("baby.grown",
                                            "<prefix><gray>Tu hij<villager_sex_endchar> <villager_name> a crecido.")),
                                    vil);
                        });
                        Optional.ofNullable(getPlayerByUuid((String) tmp.get("mother"))).ifPresent(pl -> {
                            MessageUtils.MessageParsedPlaceholders((CommandSender) pl, new Message(
                                    MComesToLife.getMessages().getLang("baby.grown",
                                            "<prefix><gray>Tu hij<villager_sex_endchar> <villager_name> a crecido.")),
                                    vil);
                        });
                        DataMethods.setData("age", 1, (LivingEntity) e);
                        if (e instanceof Villager) {
                            ((Villager) e).setAdult();
                        }
                        vil.loadVillager(true);
                    }
                    vil.regen();

                }
            }
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MComesToLife.getPlugin(), new Runnable() {
            public void run() {
                try {
                    Timers.slowTimer();
                } catch (Exception exception) {
                    if(MComesToLife.isDEBUG()){
                        exception.printStackTrace();
                    }
                }
            }
        }, 120L);
    }

    public static int rand(int min, int max) {
        return (new Random()).nextInt(max - min + 1) + min;
    }

    public static void longTimer() {
        for (Player p : MComesToLife.getPlugin().getServer().getOnlinePlayers()) {
            int r = rand(1, 10);

            if (r == 1) {
                for (Entity e : p.getNearbyEntities(30.0D, 30.0D, 30.0D)) {
                    if (Timers.entEnabled(e)) {
                        LivingEntity v = (LivingEntity) e;
                        CustomVillager cv = new CustomVillager(v);
                        cv.loadVillager(true);
                        if (cv.getLikes(p).orElse(0) > 0) {
                            // to-do Give Flower
                            break;
                        }
                        Sex vSex = cv.getSex();
                        Sex pSex = Sex.valueOf(Optional.ofNullable(DataMethods.retrivePlayerData(p).get("sex"))
                                .orElse("male").toString());// this.saveFile.getString("players." + p.getUniqueId() +
                                                            // ".sex");
                        if (!pSex.equals(vSex)) {
                            cv.addLikes(FileUtils.getFileConfiguration("config.yml")
                                    .getInteger("config.give-likes-ontime", 10), p);
                            break;
                        }
                    }
                }
            }
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MComesToLife.getPlugin(), new Runnable() {
            public void run() {
                try {
                    Timers.longTimer();
                } catch (Exception exception) {
                    if(MComesToLife.isDEBUG()){
                        exception.printStackTrace();
                    }
                }
            }
        }, (FileUtils.getFileConfiguration("config.yml").getInteger("config.likes-timer", 40) * 20));
    }
}
