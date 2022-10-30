package dev.arubik.mctl.entity;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.holders.Timers;
import dev.arubik.mctl.utils.ShortCuts;

public class WorldEntityLoader {

    public static void makeVillagers() {
        for (World w : MComesToLife.getPlugin().getServer().getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e.getLocation().getChunk().isLoaded()) {
                    if (Timers.entEnabled(e)) {
                        ShortCuts.Sync(new Runnable() {
                            @Override
                            public void run() {
                                CustomVillager customVillager = new CustomVillager((LivingEntity) e);
                                customVillager.loadVillager(true);
                            }
                        });
                    }
                }
            }
        }
    }
}
