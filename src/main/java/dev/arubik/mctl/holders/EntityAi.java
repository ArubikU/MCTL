package dev.arubik.mctl.holders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderFollowMob;
import me.gamercoder215.mobchip.ai.goal.PathfinderFollowOwner;
import me.gamercoder215.mobchip.ai.goal.PathfinderFollowParent;
import me.gamercoder215.mobchip.ai.goal.PathfinderMoveToBlock;
import me.gamercoder215.mobchip.ai.goal.WrappedPathfinder;
import me.gamercoder215.mobchip.ai.memories.EntityMemory;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.Works;
import dev.arubik.mctl.holders.IA.PlayerFollow;
import dev.arubik.mctl.holders.IA.VillagerDefend;
import dev.arubik.mctl.utils.messageUtils;

public class EntityAi {
    public void setupAi(CustomVillager mob) {
        Works work = mob.getWork();
    }

    public static List<WrappedPathfinder> getPathsfinder(Mob m) {
        EntityBrain brain = BukkitBrain.getBrain(m);
        EntityAI goal = brain.getGoalAI();
        return goal.stream().filter(p -> p.getPriority() == 1).collect(Collectors.toList());
    }

    public static void follow(Mob m, Player owner) {
        // if(io.papermc.paper.entit)
        EntityBrain brain = BukkitBrain.getBrain(m);
        EntityAI goal = brain.getGoalAI();
        EntityAI target = brain.getTargetAI();
        final PlayerFollow follow = new PlayerFollow(m, owner);

        // target.put(follow, 0);
        goal.put(follow, 0);
    }

    public static void stopFollow(Mob m) {
        EntityBrain brain = BukkitBrain.getBrain(m);
        EntityAI target = brain.getTargetAI();
        EntityAI goal = brain.getGoalAI();
        goal.removeIf(p -> p.getPathfinder().getInternalName().equalsIgnoreCase("PlayerFollow"));
    }

    public static void stay(Mob m) {
        EntityBrain brain = BukkitBrain.getBrain(m);
    }

    public static void SetHome(Mob m, Location loc) {
        EntityBrain brain = BukkitBrain.getBrain(m);
        EntityAI goal = brain.getGoalAI();
        brain.setMemory(EntityMemory.HOME, loc);
    }

    public static Boolean goHome(Mob m) {
        EntityBrain brain = BukkitBrain.getBrain(m);
        EntityAI goal = brain.getGoalAI();
        EntityAI target = brain.getTargetAI();
        if (brain.getMemory(EntityMemory.HOME) == null)
            return false;
        Location loc = brain.getMemory(EntityMemory.HOME);
        brain.getController().moveTo(loc, brain.getController().getCurrentSpeedModifier());
        return true;
    }

    public static void startFishing(CustomVillager v) {
        Mob m = (Mob) v.getLivingEntity();
        EntityBrain brain = BukkitBrain.getBrain(m);
        EntityAI goal = brain.getGoalAI();
        EntityAI target = brain.getTargetAI();
    }

    public static void updateVillagerDefend(Mob m) {

        Bukkit.getScheduler().runTaskLater(MComesToLife.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    if (m == null)
                        messageUtils.Bukkitlog("[NonDebuggingIsAError!] false");
                    if (m == null)
                        return;
                    EntityBrain brain = BukkitBrain.getBrain(m);
                    EntityAI goal = brain.getGoalAI();
                    final VillagerDefend follow = new VillagerDefend(m, true, EntityType.VINDICATOR, EntityType.ZOMBIE,
                            EntityType.DROWNED, EntityType.WITCH);
                    Bukkit.getScheduler().runTaskLater(MComesToLife.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            goal.removeIf(p -> p.getPathfinder().getInternalName().equalsIgnoreCase("VillagerAttack"));
                        }

                    }, 1L);
                    goal.put(follow, 0);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        }, 0L);
    }

}
