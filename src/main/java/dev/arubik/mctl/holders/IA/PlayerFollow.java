package dev.arubik.mctl.holders.IA;

import org.bukkit.Material;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.gamercoder215.mobchip.ai.SpeedModifier;
import me.gamercoder215.mobchip.ai.goal.CustomPathfinder;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.Ranged;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;

// Bukkit Imports...

public class PlayerFollow extends CustomPathfinder {
    public Player p;

    public PlayerFollow(@NotNull Mob m, Player p) {
        super(m);
        this.p = p;
    }

    @Override
    public boolean canStart() {
        return entity.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR;
    }

    // Automatically called when canStart() returns true.
    // In this case, We're using the EntityController (more on that below) to
    // control the entity's movements.

    @Override
    public void start() {
        BukkitBrain.getBrain(entity).getController().jump().moveTo(p.getLocation());
    }

    // Automatically called when canStart() returns false.
    // We don't need to put down anything.

    @Override
    public void tick() {
        BukkitBrain.getBrain(entity).getController().jump().moveTo(p.getLocation());
    }

    @Override
    public PathfinderFlag[] getFlags() {
        return new PathfinderFlag[] { PathfinderFlag.MOVEMENT, PathfinderFlag.JUMPING };
    }
    @Override
    public String getInternalName() {
        return "PlayerFollow";
    }
}