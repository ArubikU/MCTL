package dev.arubik.mctl.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.events.Listener;
import dev.arubik.mctl.holders.Methods.DataMethods;
import io.papermc.paper.event.entity.EntityPortalReadyEvent;

public class PaperEvents extends Listener {

    @EventHandler
    public void EntityPortalEvent(EntityPortalReadyEvent event) {
        if (dev.arubik.mctl.holders.Timers.entEnabled(event.getEntity())
                && DataMethods.avaliable(event.getEntity())) {
            Bukkit.getScheduler().runTaskLater(MComesToLife.getPlugin(), new Runnable() {

                @Override
                public void run() {
                    dev.arubik.mctl.entity.CustomVillager customVillager = new dev.arubik.mctl.entity.CustomVillager(
                            ((LivingEntity) event.getEntity()));
                    customVillager.loadVillager(true);
                }

            }, 1);
        }
    }
}
