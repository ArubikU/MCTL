package dev.arubik.mctl.holders.IA;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.BetterEntity;
import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.ItemSerializer;
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

public class VillagerDefend extends me.gamercoder215.mobchip.ai.goal.CustomPathfinder {

    public List<EntityType> Targets = new ArrayList<EntityType>();
    private Boolean needSee = false;
    private final static long COOLDOWN_BETWEEN_ATTACKS = 10L;
    private final static long COOLDOWN_BETWEEN_BOW_CHARGUE = 2L;
    private EntityBrain brain;
    private BetterEntity eBetterEntity;
    private Boolean isCrossBow = false;
    private Boolean offHandShield = false;

    public VillagerDefend(@NotNull Mob m) {
        super(m);
        eBetterEntity = new BetterEntity(m);
        brain = eBetterEntity.getBrain();
    }

    public VillagerDefend(@NotNull Mob m, EntityType... targets) {
        this(m);
        for (EntityType type : targets) {
            this.Targets.add(type);
        }
    }

    public VillagerDefend(@NotNull Mob m, Boolean needSee, EntityType... targets) {
        this(m, targets);
        this.needSee = needSee;
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public @NotNull PathfinderFlag[] getFlags() {
        return new PathfinderFlag[] { PathfinderFlag.MOVEMENT, PathfinderFlag.JUMPING, PathfinderFlag.TARGETING };
    }

    public Boolean avaliableToAttack() {
        if (getEntity().getEquipment().getItemInOffHand().getType().equals(Material.SHIELD)) {
            offHandShield = true;
        }

        if (ItemSerializer.getType(getEntity().getEquipment().getItemInMainHand()).toUpperCase().contains("AXE")
                || ItemSerializer.getType(getEntity().getEquipment().getItemInMainHand()).toUpperCase()
                        .contains("SWORD")) {
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
        tick();
    }

    public void setupAttack() {
        if (isBoweable()) {
            if (brain.getMemory(EntityMemory.ATTACK_COOLING_DOWN))
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
                        eBetterEntity.performRangedAttack(brain.getMemory(EntityMemory.ATTACK_TARGET), force);
                    }

                    brain.getBody().useItem(InteractionHand.OFF_HAND);
                }
                brain.setMemory(EntityMemory.ATTACK_COOLING_DOWN, true, COOLDOWN_BETWEEN_BOW_CHARGUE);
            } else {
                float force = 1.0f;
                eBetterEntity.performRangedAttack(brain.getMemory(EntityMemory.ATTACK_TARGET), force);
                brain.setMemory(EntityMemory.ATTACK_COOLING_DOWN, true, COOLDOWN_BETWEEN_BOW_CHARGUE + 2L);
            }
        } else {
            if (brain.getMemory(EntityMemory.ATTACK_COOLING_DOWN))
                return;
            brain.setMemory(EntityMemory.ATTACK_COOLING_DOWN, true, COOLDOWN_BETWEEN_ATTACKS);
            brain.getBody().useItem(InteractionHand.MAIN_HAND);
            getEntity().swingMainHand();
            getEntity().attack(getTarget());
        }
        // getEntity().getEquipment().getItemInMainHand();

    }

    public LivingEntity getTarget() {
        LivingEntity target = eBetterEntity.getLivingNearbyOfEntityes(
                eBetterEntity.getNearbyEntities(MComesToLife.config.getInt("config.attack-radius", 4)));
        if (brain.containsMemory(EntityMemory.ATTACK_TARGET)) {
            target = brain.getMemory(EntityMemory.ATTACK_TARGET);
        }
        if (needSee) {
            if (!brain.canSee(target))
                return null;
        }

        if (Targets.isEmpty()) {
            return target;
        }
        if (!Targets.contains(target.getType()))
            return null;
        return target;
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
                eBetterEntity.getNMSEntity().releaseUsingItem();
            }

        }
        if (eBetterEntity.getNBT(CustomMemory.SHIELDING) != null) {
            return;
        }

        if (DataMethods.rand(0, 3) == 0 && eBetterEntity.getNBT(CustomMemory.NEXT_SHIELDING) == null) {
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
        if (getTarget() != null && avaliableToAttack()) {
            BukkitBrain.getBrain(entity).getController().lookAt(getTarget()).moveTo(getTarget());
            setupAttack();
        }
    }

    @Override
    public String getInternalName() {
        return "VillagerAttack";
    }

}
