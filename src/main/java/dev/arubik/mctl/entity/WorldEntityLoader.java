package dev.arubik.mctl.entity;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.holders.timers;
import dev.arubik.mctl.holders.Methods.DataMethods;

public class WorldEntityLoader {

    public static void makeVillagers() {
        for (World w : MComesToLife.getPlugin().getServer().getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e.getLocation().getChunk().isLoaded()) {
                    if (timers.entEnabled(e)) {
                        CustomVillager customVillager = new CustomVillager((LivingEntity) e);
                        customVillager.loadVillager(true);
                    }
                }
            }
        }
    }
}
