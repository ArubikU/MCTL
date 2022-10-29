package dev.arubik.mctl.events.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.GenericGameEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.lumine.mythic.lib.api.crafting.uifilters.RecipeUIFilter;
import io.lumine.mythic.lib.comp.mythicmobs.MythicMobsHook;
import me.gamercoder215.mobchip.ai.memories.EntityMemory;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.BetterEntity;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.EventType;
import dev.arubik.mctl.enums.Mood;
import dev.arubik.mctl.events.Listener;
import dev.arubik.mctl.events.event.CustomEvent;
import dev.arubik.mctl.holders.EntityAi;
import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.Timers;
import dev.arubik.mctl.holders.IA.CustomMemory;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.holders.VillagerInventoryHolder.Material;
import dev.arubik.mctl.utils.GuiCreator;
import dev.arubik.mctl.utils.FileUtils;
import dev.arubik.mctl.utils.MessageUtils;
import dev.arubik.mctl.utils.GuiCreator.GuiHolder;

public class EntityListener extends Listener {

    public EntityListener() {
        super();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager))
            return;

        ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());
        if (item == null)
            return;
        if (item.getType() == org.bukkit.Material.NAME_TAG)
            event.setCancelled(true);
    }

    /*
     * on death entity type villager timer.ent(entity) if true then
     * remove their data and tell their family they dead
     */
    @EventHandler
    public void onDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        if (dev.arubik.mctl.holders.Timers.entEnabled(event.getEntity())) {
            dev.arubik.mctl.entity.CustomVillager customVillager = new dev.arubik.mctl.entity.CustomVillager(
                    event.getEntity());

            CustomEvent customEvent = new CustomEvent(customVillager);
            customEvent.putParam("TYPE", EventType.VILLAGERDEAD);
            customEvent.Invoke();
            if (customEvent.isCancelled()) {
                customVillager.regen(10);
                event.setCancelled(true);
                return;
            }

            MessageUtils.BukkitLog(event.getEntity() + event.getEntity().getUniqueId().toString() + " died");

            DataMethods.setData("death", true, customVillager.villager);
            // play death sound
            customVillager.villager.getWorld().playSound(customVillager.villager.getLocation(), "entity.player.death",
                    1, 1);
            customVillager.killVillager();

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                Inventory openInventory = player.getOpenInventory().getTopInventory();
                if (!(openInventory.getHolder() instanceof GuiHolder))
                    continue;

                GuiHolder inv = (GuiHolder) openInventory.getHolder();
                if (inv.getInventoryData().get("villager") == null)
                    continue;
                LivingEntity villager = (LivingEntity) inv.getInventoryData().get("villager");
                if (villager.getUniqueId() == customVillager.villager.getUniqueId()) {
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof IronGolem))
            return;
        if (!(DataMethods.isVillager(event.getTarget())))
            return;

        // Prevent iron golem attacking villagers (they might hit them by accident with
        // a bow/crossbow).
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityTransformEvent(EntityTransformEvent event) {
        if (!(event.getEntity() instanceof Villager))
            return;
        if (!(DataMethods.isVillager(event.getEntity())))
            return;
        if ((event.getTransformReason() == EntityTransformEvent.TransformReason.INFECTION)) {
            event.getTransformedEntity().remove();
            // event.setCancelled(true);
        }
        if (event.getTransformReason() == EntityTransformEvent.TransformReason.CURED) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGenericGameEvent(GenericGameEvent event) {
        GameEvent gameEvent = event.getEvent();

        // RING_BELL is deprecated and shouldn't be used, but due to having the same key
        // as BLOCK_CHANCE,
        // RING BELL is called because of the map replacing the duplicated key.
        if (gameEvent != GameEvent.BLOCK_CHANGE && gameEvent != GameEvent.RING_BELL)
            return;
        event.getLocation().getBlock().getType();
        if (event.getLocation().getBlock().getType() != org.bukkit.Material.BELL)
            return;

        // Play swing hand animation when ringing bell.
        if (DataMethods.isCustom(event.getEntity())) {
            ((LivingEntity) event.getEntity()).swingMainHand();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {

        if (!(event.getEntity().getShooter() instanceof Villager))
            return;
        if (!(event.getHitEntity() instanceof Villager))
            return;

        event.setCancelled(true);
    }

    /*
     * image.png
     * on summon a villager and verify they dont have any nbt tags in the List of
     * path config.yml config.except-nbts entity
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSummon(org.bukkit.event.entity.CreatureSpawnEvent event) {
        if (DataMethods.avaliable(event.getEntity()) && Timers.entEnabled(event.getEntity())) {
            dev.arubik.mctl.entity.CustomVillager customVillager = new dev.arubik.mctl.entity.CustomVillager(
                    event.getEntity());
            customVillager.loadVillager(true);
        }
    }

    public static List<String> playertoTrade = new ArrayList<String>();
    public static List<String> playersToSendMessageOfPunch = new ArrayList<String>();

    // on hit entity event
    @EventHandler
    public void onHit(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        // setup the villager transformation to zombie
        if (event.getEntity().isDead()) {
            return;
        }
        if (!Timers.entEnabled(event.getEntity())) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        if (DataMethods.isVillager(event.getEntity())) {
            if (!(event.getDamager() instanceof Player && !EntityAi.targetTypes.contains("PLAYER"))) {
                BetterEntity betterEntity = BetterEntity.getFromEntity((LivingEntity) event.getEntity());
                betterEntity.setNBT(CustomMemory.TARGET_UUID, event.getDamager().getUniqueId().toString());
            }
        }
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (playersToSendMessageOfPunch.contains(event.getDamager().getUniqueId().toString())) {
                return;
            }
            if (DataMethods.avaliable(event.getEntity())) {
                CustomVillager cv = new CustomVillager((LivingEntity) event.getEntity());
                cv.loadVillager(false);
                if (cv.getData().containsKey("death")) {
                    return;
                }
                cv.getLivingEntity().getWorld().playSound(cv.getLivingEntity().getLocation(), "entity.player.hurt", 1,
                        1);
                cv.getLivingEntity().removePotionEffect(PotionEffectType.SLOW);
                cv.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 2));
                if (Mood.rand(1, 15) == 10) {
                    cv.getLivingEntity().attack(event.getDamager());
                }
                cv.takeLikes(Mood.rand(1, 10), player);
                // play angry particles upp the villager

                playersToSendMessageOfPunch.add(event.getDamager().getUniqueId().toString());
                Bukkit.getScheduler().runTaskLater(MComesToLife.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        if (playersToSendMessageOfPunch.contains(event.getDamager().getUniqueId().toString())) {
                            playersToSendMessageOfPunch.remove(event.getDamager().getUniqueId().toString());
                            MessageUtils.MessageParsedPlaceholders((CommandSender) player,
                                    new Message(Mood.getText("punch",
                                            DataMethods.getFamily("punch", player, cv.getLivingEntity()))),
                                    cv);
                        }
                    }

                }, 0);
            }
        }
    }

    @EventHandler
    public void VehicleEnterEvent(org.bukkit.event.vehicle.VehicleEnterEvent event) {
        if (event.getEntered() instanceof Villager && event.getVehicle().getType() == EntityType.BOAT
                && MComesToLife.getMainConfig().getBoolean("config.unboat", false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRightClickEntity(org.bukkit.event.player.PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (Timers.entEnabled(entity)) {
            if (!DataMethods.isCustom(event.getRightClicked())) {
                CustomVillager vil = new CustomVillager((LivingEntity) entity);
                vil.loadVillager(true);
            }
            MComesToLife.lastClickedEntity.put(player.getUniqueId().toString(), entity);
            if (playertoTrade.contains(player.getUniqueId().toString().toLowerCase())) {
                playertoTrade.remove(player.getUniqueId().toString().toLowerCase());
                return;
            } else if (player.isSneaking()) {
                return;
            } else {
                event.setCancelled(true);
                MComesToLife.Maingui.OpenInv(player);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(org.bukkit.event.world.ChunkLoadEvent e) {
        for (Entity entity : e.getChunk().getEntities()) {
            if (Timers.entEnabled(entity)) {
                CustomVillager customVillager = new CustomVillager((LivingEntity) entity);
                customVillager.loadVillager(true);
            }
        }
    }

    // @EventHandler
    public void onEntityLoad(org.bukkit.event.world.EntitiesLoadEvent e) {
        for (Entity entity : e.getEntities()) {
            if (Timers.entEnabled(entity) && e.getChunk().isLoaded()) {
                CustomVillager customVillager = new CustomVillager((LivingEntity) entity);
                customVillager.loadVillager(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
        for (Entity entity : e.getPlayer().getNearbyEntities(10, 10, 10)) {
            if (DataMethods.isCustom(entity)) {
                EntityAi.pickupItem((Mob) entity, e.getItemDrop());
            }
        }
    }

    // @EventHandler
    // public void onVillagerDropItemEvent(EntityDropItemEvent e){
    // if(DataMethods.isCustom(e.getEntity())){
    // for(Entity entity: e.getEntity().getNearbyEntities(10, 10, 10)){
    // if(entity.getUniqueId()==e.getEntity().getUniqueId()){
    // continue;
    // }
    // if(DataMethods.isCustom(entity)){
    // EntityAi.pickupItem((Mob)entity, e.getItemDrop());
    // }
    // }
    // }
    // }

}
