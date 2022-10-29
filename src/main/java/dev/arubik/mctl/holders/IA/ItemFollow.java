package dev.arubik.mctl.holders.IA;

import org.bukkit.Material;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.NotNull;

import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.utils.MessageUtils;
import me.gamercoder215.mobchip.ai.SpeedModifier;
import me.gamercoder215.mobchip.ai.goal.CustomPathfinder;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.Ranged;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;

// Bukkit Imports...

public class ItemFollow extends CustomPathfinder {
    public Item p;

    public ItemFollow(@NotNull Mob m,@NotNull Item p) {
        super(m);
        this.p = p;
    }

    @Override
    public boolean canStart() {
        if (p == null || p.isDead() || p.getLocation().distance(entity.getLocation()) > 10) {
            return false;
        }
        return p.canMobPickup();
    }

    // Automatically called when canStart() returns true.
    // In this case, We're using the EntityController (more on that below) to
    // control the entity's movements.

    @Override
    public void start() {
        tick();
    }

    // Automatically called when canStart() returns false.
    // We don't need to put down anything.

    @Override
    public void tick() {
        if (!canStart()) {
            BukkitBrain.getBrain(entity).getGoalAI()
                    .removeIf(path -> path.getPathfinder().getInternalName().equalsIgnoreCase("ItemFollow"));
            MessageUtils.BukkitLog("config.hight-debug", "ItemFollow removed");
            return;
        }
        ;
        BukkitBrain.getBrain(entity).getController().jump().moveTo(p.getLocation());
        if (entity.getLocation().distance(p.getLocation()) < 1) {
            VillagerInventoryHolder holder = new VillagerInventoryHolder(entity);
            holder.loadInventory();
            if (holder.canTakeItems()) {
                holder.addItem(p.getItemStack());
                p.remove();
            }
            BukkitBrain.getBrain(entity).getGoalAI()
                    .removeIf(path -> path.getPathfinder().getInternalName().equalsIgnoreCase("ItemFollow"));
        }
    }

    @Override
    public PathfinderFlag[] getFlags() {
        return new PathfinderFlag[] { PathfinderFlag.MOVEMENT, PathfinderFlag.JUMPING };
    }

    @Override
    public String getInternalName() {
        return "ItemFollow";
    }
}
