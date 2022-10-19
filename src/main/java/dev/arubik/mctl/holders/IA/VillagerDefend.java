package dev.arubik.mctl.holders.IA;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.BetterEntity;
import dev.arubik.mctl.enums.Version;
import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.ItemSerializer;
import dev.arubik.mctl.utils.MessageUtils;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.EntityBody.InteractionHand;
import me.gamercoder215.mobchip.abstraction.ChipUtil;
import me.gamercoder215.mobchip.ai.SpeedModifier;
import me.gamercoder215.mobchip.ai.animation.EntityAnimation;
import me.gamercoder215.mobchip.ai.goal.CustomPathfinder;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.Ranged;
import me.gamercoder215.mobchip.ai.memories.EntityMemory;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class VillagerDefend extends me.gamercoder215.mobchip.ai.goal.CustomPathfinder {

    public List<String> Targets = new ArrayList<String>();
    public List<String> TargetsIDS = new ArrayList<String>();
    private Boolean needSee = false;
    private final static long COOLDOWN_BETWEEN_ATTACKS = 20L;
    private final static long COOLDOWN_BETWEEN_BOW_CHARGUE = 5L;
    private EntityBrain brain;
    private BetterEntity eBetterEntity;
    private Boolean isCrossBow = false;
    private Boolean offHandShield = false;

    public VillagerDefend(@NotNull Mob m) {
        super(m);
        eBetterEntity = new BetterEntity(m);
        brain = eBetterEntity.getBrain();
    }

    public void addID(String mobID) {
        TargetsIDS.add(mobID);
    }

    public VillagerDefend(@NotNull Mob m, String... targets) {
        this(m);
        for (String type : targets) {
            this.Targets.add(type);
        }
    }

    public VillagerDefend(@NotNull Mob m, Boolean needSee, String... targets) {
        this(m, targets);
        this.needSee = needSee;
    }

    public VillagerDefend(@NotNull Mob m, Boolean needSee, List<String> targets) {
        this(m);
        this.Targets = targets;
        this.needSee = needSee;
    }

    public VillagerDefend(@NotNull Mob m, List<String> targets) {
        this(m);
        this.Targets = targets;
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public @NotNull PathfinderFlag[] getFlags() {
        return new PathfinderFlag[] { PathfinderFlag.MOVEMENT, PathfinderFlag.JUMPING, PathfinderFlag.TARGETING };
    }

    public Boolean avaliableToAttack(LivingEntity target) {
        MessageUtils.BukkitLog(target + "");
        if (target == null)
            return false;
        if (getEntity().getEquipment().getItemInOffHand().getType().equals(Material.SHIELD)) {
            offHandShield = true;
        }

        if (target != null) {
            if (BetterEntity.getFromEntity(target).getNMSEntity().isDeadOrDying())
                return false;
        }

        String type = ItemSerializer.getType(eBetterEntity.getLivingEntity().getEquipment().getItemInMainHand())
                .toUpperCase();

        if (type.contains("AXE")
                || type.contains("SWORD")
                || type.contains("TRIDENT")) {
            return true;
        }
        return false;
    }

    public Boolean isBoweable() {
        if (getEntity().getEquipment().getItemInMainHand().getType().toString().toUpperCase().contains("BOW")) {
            if (getEntity().getEquipment().getItemInMainHand().getType().toString().toUpperCase().contains("CROSS")) {
                isCrossBow = true;
            }
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        eBetterEntity.removeNBT(CustomMemory.TARGET_UUID);
        tick();
    }

    public void setupAttack(LivingEntity target) {
        if (isBoweable()) {
            if (eBetterEntity.getNBT(EntityMemory.ATTACK_COOLING_DOWN) == null)
                return;
            if (eBetterEntity.getNBT(EntityMemory.ATTACK_COOLING_DOWN))
                return;
            if (isCrossBow) {
                if (eBetterEntity.getNBT(CustomMemory.CROSSBOW) == null) {
                    eBetterEntity.setNBT(CustomMemory.CROSSBOW, CustomMemory.CrossbowState.CHARGING.toString());
                }
                if (Objects.equals(eBetterEntity.getNBT(CustomMemory.CROSSBOW).toString(),
                        CustomMemory.CrossbowState.CHARGING.toString())) {
                    eBetterEntity.setNBT(CustomMemory.CROSSBOW, CustomMemory.CrossbowState.CHARGED.toString());
                }
                if (Objects.equals(eBetterEntity.getNBT(CustomMemory.CROSSBOW).toString(),
                        CustomMemory.CrossbowState.CHARGED.toString())) {
                    eBetterEntity.setNBT(CustomMemory.CROSSBOW,
                            CustomMemory.CrossbowState.READY_TO_ATTACK.toString());
                }
                if (Objects.equals(eBetterEntity.getNBT(CustomMemory.CROSSBOW).toString(),
                        CustomMemory.CrossbowState.READY_TO_ATTACK.toString())) {
                    eBetterEntity.removeNBT(CustomMemory.CROSSBOW);

                    boolean multishot = ItemSerializer.getEnchantmentLevel(entity.getEquipment().getItemInMainHand(),
                            "MULTISHOT") > 0;

                    for (int i = 0; i < (isCrossBow && multishot ? 3 : 1); i++) {
                        float force = (i == 0) ? (isCrossBow ? 0.0f : 1.0f) : (i == 1) ? 10.0f : -10.0f;
                        eBetterEntity.performRangedAttack(target, force);
                    }

                    brain.getBody().useItem(InteractionHand.OFF_HAND);
                }
                eBetterEntity.setNBTTime(EntityMemory.ATTACK_COOLING_DOWN, true, COOLDOWN_BETWEEN_BOW_CHARGUE);

            } else {
                float force = 1.0f;
                eBetterEntity.performRangedAttack(target, force);
                eBetterEntity.setNBTTime(EntityMemory.ATTACK_COOLING_DOWN, true, COOLDOWN_BETWEEN_ATTACKS + 2L);
            }
        } else {
            if (eBetterEntity.getBrain().canSee(target)
                    && eBetterEntity.getNearbyEntities(MComesToLife.getMainConfig().getInt("config.attack-radius", 2))
                            .contains(target)) {
                if (eBetterEntity.getNBT(EntityMemory.ATTACK_COOLING_DOWN) == null) {
                    eBetterEntity.setNBTTime(EntityMemory.ATTACK_COOLING_DOWN, true, COOLDOWN_BETWEEN_ATTACKS);
                    eBetterEntity.performAttack(target);
                    return;
                }
                if (eBetterEntity.getNBT(EntityMemory.ATTACK_COOLING_DOWN))
                    return;
                eBetterEntity.setNBTTime(EntityMemory.ATTACK_COOLING_DOWN, true, COOLDOWN_BETWEEN_ATTACKS);
                eBetterEntity.performAttack(target);
            }
        }

    }

    public LivingEntity getTarget() {
        LivingEntity target = this.eBetterEntity.getLivingEntity();
        if (eBetterEntity.getNBT(CustomMemory.TARGET_UUID) != null) {
            target = eBetterEntity.getLivingTarget();
            if (isAvaliableTarget(target)) {
                return target;
            }
        }
        if (eBetterEntity.getLivingTarget() == null
                || (eBetterEntity.getLivingTarget() != null && !isAvaliableTarget(eBetterEntity.getLivingTarget()))) {
            List<LivingEntity> targets = getAvaliableTargets(
                    eBetterEntity.getNearbyEntities(MComesToLife.getMainConfig().getInt("config.follow-radius", 6)));
            MessageUtils.BukkitLog(Arrays.deepToString(
                    eBetterEntity.getNearbyEntities(MComesToLife.getMainConfig().getInt("config.follow-radius",
                            6))
                            .toArray()));
            return eBetterEntity.getLivingNearbyOfLivingEntityes(targets);
        }
        return null;
    }

    private Boolean isAvaliableTarget(@Nullable LivingEntity target) {
        if (target == null)
            return false;
        if (needSee) {
            if (!brain.canSee(target))
                return false;
        }

        if (Targets.isEmpty() && TargetsIDS.isEmpty()) {
            return false;
        }

        if (MComesToLife.getEnabledPlugins().isEnabled("MythicMobs")) {
            BukkitAPIHelper Mythic = MythicBukkit.inst().getAPIHelper();
            if (Mythic.isMythicMob(target)) {
                MessageUtils.BukkitLog("IsMythicMob :(");
                if (TargetsIDS.contains(
                        Mythic.getMythicMobInstance(target).getType().getInternalName())) {
                    MessageUtils.BukkitLog("MythicID: " + Mythic
                            .getMythicMobInstance(target).getType().getInternalName());
                    return true;
                } else {
                    MessageUtils.BukkitLog("Not Finded in list: " + Mythic
                            .getMythicMobInstance(target).getType().getInternalName());
                }
                return false;
            }
        }

        if (!Targets.contains(target.getType().toString()))
            return false;
        return true;
    }

    private List<LivingEntity> getAvaliableTargets(List<Entity> targets) {
        List<LivingEntity> avaliableTargets = new ArrayList<>();
        for (Entity target : targets) {
            if (target instanceof LivingEntity) {
                if (isAvaliableTarget((LivingEntity) target)) {
                    avaliableTargets.add((LivingEntity) target);
                }
            }
        }
        return avaliableTargets;
    }

    @Override
    public void tick() {
        if (this.getEntity() instanceof Villager) {
            if (!((Villager) this.getEntity()).isAdult()) {
                return;
            }
        }
        if (eBetterEntity.getNBT(CustomMemory.SHIELDING) == null) {
            if (eBetterEntity.getNMSEntity() != null) {
                if (MComesToLife.getServerVersion().contains("1_19")) {
                    eBetterEntity.releaseUsingItem(Version.v1_19_R1);
                } else {
                    eBetterEntity.releaseUsingItem(Version.v1_18_R1);
                }
            }
        }

        if (DataMethods.rand(0, 4) == 0 && eBetterEntity.getNBT(CustomMemory.NEXT_SHIELDING) == null && offHandShield) {
            brain.getBody().useItem(InteractionHand.OFF_HAND);
            Long nextShielding = 1L;
            if (DataMethods.rand(1, 2) == 1) {
                nextShielding = 2L;
            }
            eBetterEntity.setNBTTime(CustomMemory.SHIELDING, true, nextShielding);
            eBetterEntity.setNBTTime(CustomMemory.NEXT_SHIELDING, true, nextShielding + 1L);
            eBetterEntity.getNMSEntity().startUsingItem(net.minecraft.world.InteractionHand.OFF_HAND);
            return;
        }
        LivingEntity target = getTarget();
        if (avaliableToAttack(target)) {
            MessageUtils.BukkitLog("Avaliable to attack");
            BukkitBrain.getBrain(entity).getController().lookAt(target).moveTo(target);
            setupAttack(target);
        }
    }

    @Override
    public String getInternalName() {
        return "VillagerAttack";
    }

}
