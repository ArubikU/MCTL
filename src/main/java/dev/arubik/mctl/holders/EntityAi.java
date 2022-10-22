package dev.arubik.mctl.holders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;

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
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.FileConfiguration;
import dev.arubik.mctl.utils.ItemSerializer;
import dev.arubik.mctl.utils.FileUtils;
import dev.arubik.mctl.utils.MessageUtils;
import dev.arubik.mctl.utils.Json.LineConfig;
import dev.lone.itemsadder.api.CustomStack;

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

    private static List<String> aaaa = new ArrayList<String>();
    private static List<String> empty = new ArrayList<String>();
    private static Material AIR = Material.AIR;
    static {
        aaaa.add("ZOMBIE");
    }

    public static ItemStack getRandomItem(Profession proffesion) {
        ItemStack returned = new ItemStack(AIR);
        List<String> items = MComesToLife.getMainConfig()
                .getStringList("config." + proffesion.toString().toLowerCase() + "-items", empty);
        if (items.isEmpty())
            return returned;
        String item = items.get(DataMethods.rand(0, items.size()));
        switch (item.split(":")[0].toLowerCase()) {
            case "item":
                returned = new ItemStack(Material.getMaterial(item.split(":")[1]));
                break;
            case "itemsadder":
                if (MComesToLife.getEnabledPlugins().isEnabled("ItemsAdder"))
                    returned = CustomStack.getInstance(item.replaceFirst("itemsadder:", "")).getItemStack();
                break;
            case "config": {
                LineConfig config = new LineConfig(item);
                FileConfiguration configFile = FileUtils.getFileConfiguration(config.getString("path", "items.yml"));
                if (configFile.getConfig().contains(config.getString("section"))) {
                    returned = ItemSerializer.getFromConfigurationSection(
                            configFile.getConfig().getConfigurationSection(config.getString("section")));
                }
                break;
            }
        }
        return returned;

    }

    public static ItemStack getRandomItem(EntityType proffesion) {
        ItemStack returned = new ItemStack(AIR);
        List<String> items = MComesToLife.getMainConfig()
                .getStringList("config." + proffesion.toString().toLowerCase() + "-items", empty);
        if (items.isEmpty())
            return returned;
        String item = items.get(DataMethods.rand(0, items.size())).toLowerCase();
        switch (item.split(":")[0]) {
            case "item":
                returned = new ItemStack(Material.getMaterial(item.split(":")[1]));
                break;
            case "itemsadder":
                if (MComesToLife.getEnabledPlugins().isEnabled("ItemsAdder"))
                    if (CustomStack.getInstance(item.replaceFirst("itemsadder:", "")) != null)
                        returned = CustomStack.getInstance(item.replaceFirst("itemsadder:", "")).getItemStack();
                break;
            case "config": {
                LineConfig config = new LineConfig(item);
                FileConfiguration configFile = FileUtils.getFileConfiguration(config.getString("path", "items.yml"));
                if (configFile.getConfig().contains(config.getString("section"))) {
                    returned = ItemSerializer.getFromConfigurationSection(
                            configFile.getConfig().getConfigurationSection(config.getString("section")));
                    returned = ItemSerializer.generateItem(returned);
                }
                break;
            }
        }
        return returned;

    }

    public static List<String> targetTypes = new ArrayList<String>();

    public static void reload() {
        targetTypes = MComesToLife.getMainConfig().getStringList("config.target-mobs", aaaa);
    }

    public static void addVillagerDefend(Mob m) {
        try {
            EntityBrain brain = BukkitBrain.getBrain(m);

            VillagerDefend follow = new VillagerDefend(m, true, targetTypes);
            for (String s : MComesToLife.getMainConfig().getStringList("config.mythic-target-mobs", aaaa)) {
                follow.addID(s);
            }
            // if (brain.getGoalAI().stream()
            // .anyMatch(p ->
            // p.getPathfinder().getInternalName().equalsIgnoreCase("VillagerDefend"))) {
            // brain.getGoalAI()
            // .removeIf(path ->
            // path.getPathfinder().getInternalName().equalsIgnoreCase("VillagerDefend"));
            // }

            brain.getGoalAI().put(follow, 0);
        } catch (Throwable e) {
            if(MComesToLife.isDEBUG()){
                e.printStackTrace();
            }
        }
    }

    public static void updateVillagerDefend(Mob m) {
        if (!(m instanceof Villager))
            return;
        // try {
        // EntityBrain brain = BukkitBrain.getBrain(m);
        // brain.getGoalAI().removeIf(p ->
        // p.getPathfinder().getInternalName().equalsIgnoreCase("VillagerAttack"));
        // } catch (Throwable e) {
        // e.printStackTrace();
        // }
        addVillagerDefend(m);
    }

}
