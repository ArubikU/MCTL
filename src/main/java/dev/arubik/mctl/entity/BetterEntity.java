package dev.arubik.mctl.entity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.enums.Version;
import dev.arubik.mctl.holders.EntityAi;
import dev.arubik.mctl.holders.Nms;
import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.holders.IA.CustomMemory;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.ItemSerializer;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.memories.EntityMemory;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;

public class BetterEntity extends AIEntity implements EntityMethods, CrossbowAttackMob {
    public BetterEntity(LivingEntity v) {
        super(v);
    }

    public static BetterEntity getFromEntity(LivingEntity v) {
        return new BetterEntity(v);
    }

    public void regen() {
        regen(DataMethods.rand(0, 6));
    }

    @Override
    public PlayerDisguise generateDisguise() {
        return null;
    }

    @Override
    public void Disguise() {
    }

    @Override
    public void Disguise(Player... p) {
    }

    @Override
    public void regen(int health) {
        Double newhealth = this.getLivingEntity().getHealth() + health;
        if (newhealth > this.getLivingEntity().getMaxHealth()) {
            newhealth = this.getLivingEntity().getMaxHealth();
        }
        this.getLivingEntity().setHealth(newhealth);
        this.getLivingEntity();
    }

    @Override
    public void addModifier(Attribute attribute, Double value, Operation op) {
        this.getLivingEntity().getAttribute(attribute)
                .addModifier(new AttributeModifier(UUID.randomUUID().toString(), value, op));
    }

    public void setHealth(Double value) {
        if (this.getLivingEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() > value) {
            Double toRemove = value - this.getLivingEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            addModifier(Attribute.GENERIC_MAX_HEALTH, toRemove, Operation.ADD_NUMBER);
        }
        if (this.getLivingEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() < value) {
            Double toRemove = value - this.getLivingEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            addModifier(Attribute.GENERIC_MAX_HEALTH, toRemove, Operation.ADD_NUMBER);
        }
    }

    public void setAttribute(Attribute at, Double value) {
        if (this.getLivingEntity().getAttribute(at).getValue() > value) {
            Double toRemove = value - this.getLivingEntity().getAttribute(at).getValue();
            addModifier(at, toRemove, Operation.ADD_NUMBER);
        }
        if (this.getLivingEntity().getAttribute(at).getValue() < value) {
            Double toRemove = value - this.getLivingEntity().getAttribute(at).getValue();
            addModifier(at, toRemove, Operation.ADD_NUMBER);
        }
    }

    public List<Entity> getNearbyEntities(int radius) {
        return getLivingEntity().getNearbyEntities(radius, 1, radius);
    }

    public Entity getNearbyOfEntityes(List<Entity> entities) {
        Entity closest = null;
        double distance = Double.MAX_VALUE;
        for (Entity e : entities) {
            if (e.getLocation().distance(getLivingEntity().getLocation()) < distance) {
                closest = e;
                distance = e.getLocation().distance(getLivingEntity().getLocation());
            }
        }
        return closest;
    }

    public LivingEntity getLivingNearbyOfEntityes(List<Entity> entities) {
        LivingEntity closest = null;
        double distance = Double.MAX_VALUE;
        for (Entity e : entities) {
            if (e instanceof LivingEntity) {
                if (e.getLocation().distance(getLivingEntity().getLocation()) < distance) {
                    closest = (LivingEntity) e;
                    distance = e.getLocation().distance(getLivingEntity().getLocation());
                }
            }
        }
        return closest;
    }

    public LivingEntity getLivingNearbyOfLivingEntityes(List<LivingEntity> entities) {
        if (entities.isEmpty()) {
            return null;
        }
        LivingEntity closest = null;
        double distance = Double.MAX_VALUE;
        for (LivingEntity e : entities) {
            if (e.getLocation().distance(getLivingEntity().getLocation()) < distance) {
                closest = e;
                distance = e.getLocation().distance(getLivingEntity().getLocation());
            }
        }
        return closest;
    }

    public List<LivingEntity> sortLivingEntityFromDistance(List<LivingEntity> entities) {
        List<LivingEntity> sorted = entities;
        for (int i = 0; i < sorted.size(); i++) {
            for (int j = 0; j < sorted.size(); j++) {
                if (sorted.get(i).getLocation().distance(getLivingEntity().getLocation()) < sorted.get(j)
                        .getLocation().distance(getLivingEntity().getLocation())) {
                    LivingEntity temp = sorted.get(i);
                    sorted.set(i, sorted.get(j));
                    sorted.set(j, temp);
                }
            }
        }
        return sorted;
    }

    public LivingEntity getLivingNearbyOfEntityesExcludingEntity(List<LivingEntity> entities, LivingEntity[] exclude) {
        LivingEntity closest = null;
        double distance = Double.MAX_VALUE;
        for (Entity e : entities) {
            if (e instanceof LivingEntity) {
                if (e.getLocation().distance(getLivingEntity().getLocation()) < distance) {
                    if (!Set.of(exclude).contains(e)) {
                        closest = (LivingEntity) e;
                        distance = e.getLocation().distance(getLivingEntity().getLocation());
                    }
                }
            }
        }
        return closest;
    }

    private void shootBow(net.minecraft.world.entity.LivingEntity target, Projectile projectile) {
        double deltaX = target.getX() - this.getNMSEntity().getX();
        double deltaY = target.getY(0.3333333333333333d) - projectile.getY();
        double deltaZ = target.getZ() - this.getNMSEntity().getZ();

        double horizontalMagnitude = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        projectile.shoot(
                deltaX,
                deltaY + horizontalMagnitude * 0.20000000298023224d,
                deltaZ,
                (float) 1.6,
                (float) (14 - this.getNMSEntity().getLevel().getDifficulty().getId() * 4));

        this.getNMSEntity().playSound(SoundEvents.ARROW_SHOOT, 1.0f, 1.0f / (random.nextFloat() * 0.4f + 0.8f));
    }

    public void performRangedAttack(LivingEntity target, float force) {
        performRangedAttack(new BetterEntity(target).getNMSEntity(), force);
    }

    @Override
    public void performRangedAttack(net.minecraft.world.entity.LivingEntity target, float force) {
        if (!isHoldingRangeWeapon()) {
            onCrossbowAttackPerformed();
            return;
        }
        boolean hasInfinity = false;
        if (!MComesToLife.getMainConfig().getBoolean("config.infinite-arrows", false)) {
            hasInfinity = ItemSerializer.getEnchantmentLevel(this.getLivingEntity().getEquipment().getItemInMainHand(),
                    "INFINITY") > 0;
        }
        boolean isBow = this.getNMSEntity().isHolding(Items.BOW);
        InteractionHand hand = net.minecraft.world.entity.projectile.ProjectileUtil
                .getWeaponHoldingHand(this.getNMSEntity(), this.getNMSEntity().getMainHandItem().getItem());

        ItemStack weapon = this.getNMSEntity().getItemInHand(hand);
        org.bukkit.inventory.ItemStack handItem;

        switch (hand) {
            case MAIN_HAND: {
                handItem = (getLivingEntity().getEquipment().getItemInMainHand());
                break;
            }
            case OFF_HAND: {
                handItem = (getLivingEntity().getEquipment().getItemInOffHand());
                break;
            }
            default: {
                handItem = (getLivingEntity().getEquipment().getItemInMainHand());
                break;
            }

        }

        ItemStack arrow = new ItemStack(Items.ARROW);

        org.bukkit.inventory.ItemStack arrowA = new org.bukkit.inventory.ItemStack(Material.ARROW);
        if (!hasInfinity) {
            if (ItemSerializer.getType(handItem).toUpperCase().contains("ROCKET")
                    && getLivingEntity().getEquipment().getItemInMainHand().getType() == Material.CROSSBOW) {
                arrow = new ItemStack(Items.FIREWORK_ROCKET);

                org.bukkit.inventory.ItemStack ba = this.getInventoryHolder().consumeItem("ROCKET");
                if (ba != null) {
                    arrowA = ba;
                    ItemStack temp = MComesToLife.getNms().getNMSStackFromStack(ba);
                    if (temp != null) {
                        arrow = temp;
                    }
                }
            } else {

                org.bukkit.inventory.ItemStack ba = this.getInventoryHolder().consumeItem("ARROW");
                if (ba != null) {
                    arrowA = ba;
                    ItemStack temp = MComesToLife.getNms().getNMSStackFromStack(ba);
                    if (temp != null) {
                        arrow = temp;
                    }
                }
            }

        }

        ArrowItem arrowAsItem = (ArrowItem) (arrow.getItem() instanceof ArrowItem ? arrow.getItem() : Items.ARROW);
        if (ItemSerializer.getType(handItem).toUpperCase().contains("ROCKET")
                && getLivingEntity().getEquipment().getItemInMainHand().getType() == Material.CROSSBOW) {
            arrowAsItem = (ArrowItem) (arrow.getItem() instanceof ArrowItem ? arrow.getItem() : Items.FIREWORK_ROCKET);
        }
        AbstractArrow projectile;
        if (isBow) {
            projectile = arrowAsItem.createArrow(this.getNMSEntity().getLevel(), arrow, this.getNMSEntity());
            projectile.setSoundEvent(SoundEvents.ARROW_HIT);
        } else {
            projectile = arrowAsItem.createArrow(this.getNMSEntity().getLevel(), arrow, this.getNMSEntity());
            projectile.setSoundEvent(SoundEvents.CROSSBOW_HIT);
            projectile.setShotFromCrossbow(true);
            int piercing = ItemSerializer.getEnchantmentLevel(handItem, "PIERCING");
            if (piercing > 0)
                projectile.setPierceLevel((byte) piercing);
        }
        projectile.setCritArrow(true);

        if (hasInfinity) {
            projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        if (isBow) {
            shootBow(target, projectile);
        } else {
            shootCrossbowProjectile(target, weapon, projectile, force);
        }

        weapon.hurtAndBreak(arrow.is(Items.FIREWORK_ROCKET) ? 3 : 1, this.getNMSEntity(),
                (npc) -> npc.broadcastBreakEvent(hand));
        // MComesToLife.getMainConfig().getBoolean("config.infinite-arrows", false)
        EquipmentSlot slot = EquipmentSlot.HAND;
        if (this.getNMSEntity().getUsedItemHand() == InteractionHand.OFF_HAND)
            slot = EquipmentSlot.OFF_HAND;
        EntityShootBowEvent event = new EntityShootBowEvent(villager, handItem, arrowA, villager, slot, force, isBow);
        // CraftEventFactory.callEntityShootBowEvent(
        // this,
        // weapon,
        // arrow,
        // projectile,
        // getUsedItemHand(),
        // force,
        // true);
        try {
            event.callEvent();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (event.isCancelled()) {
            if (event.getProjectile() != null) {
                if (!event.getProjectile().isDead()) {
                    event.getProjectile().remove();
                }
            }
            return;
        } else if (event.getProjectile() == projectile.getBukkitEntity()
                && !this.getNMSEntity().getLevel().addFreshEntity(projectile))
            return;

        onCrossbowAttackPerformed();
    }

    @Override
    public net.minecraft.world.entity.LivingEntity getTarget() {
        if (this.getContainerData(CustomMemory.TARGET_UUID) == null) {
            return null;
        }
        if (this.getLivingEntity().getWorld()
                .getEntity(UUID.fromString(this.getContainerData(CustomMemory.TARGET_UUID))) == null) {
            return null;
        }
        if (!(this.getLivingEntity().getWorld()
                .getEntity(UUID.fromString(this.getContainerData(CustomMemory.TARGET_UUID))) instanceof LivingEntity))
            return null;
        return new BetterEntity((LivingEntity) this.getLivingEntity().getWorld()
                .getEntity(UUID.fromString(this.getContainerData(CustomMemory.TARGET_UUID)))).getNMSEntity();
    }

    public LivingEntity getLivingTarget() {
        if (this.getContainerData(CustomMemory.TARGET_UUID) == null) {
            return null;
        }
        if (this.getLivingEntity().getWorld()
                .getEntity(UUID.fromString(this.getContainerData(CustomMemory.TARGET_UUID))) == null) {
            return null;
        }
        if (!(this.getLivingEntity().getWorld()
                .getEntity(UUID.fromString(this.getContainerData(CustomMemory.TARGET_UUID))) instanceof LivingEntity))
            return null;
        return (LivingEntity) this.getLivingEntity().getWorld()
                .getEntity(UUID.fromString(this.getContainerData(CustomMemory.TARGET_UUID)));
    }

    @Override
    public void onCrossbowAttackPerformed() {
    }

    @Override
    public void setChargingCrossbow(boolean arg0) {

    }

    @Override
    public void shootCrossbowProjectile(net.minecraft.world.entity.LivingEntity target, ItemStack arg1,
            Projectile projectile,
            float arg3) {
        shootCrossbowProjectile(this.getNMSEntity(), target, projectile, arg3, (float) 1.6);

    }

    public VillagerInventoryHolder getInventoryHolder() {
        VillagerInventoryHolder holder = new VillagerInventoryHolder(getLivingEntity());
        holder.loadInventory();
        return holder;
    }

    public boolean isHoldingWeapon() {
        return isHoldingMeleeWeapon() || isHoldingRangeWeapon();
    }

    public boolean isHoldingMeleeWeapon() {
        return this.getNMSEntity()
                .isHolding(item -> item.getItem() instanceof SwordItem || item.getItem() instanceof AxeItem);
    }

    public boolean isHoldingRangeWeapon() {
        return this.getNMSEntity()
                .isHolding(item -> item.getItem() instanceof ProjectileWeaponItem
                        && canFireProjectileWeapon((ProjectileWeaponItem) item.getItem()));
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem item) {
        return (item == Items.BOW || item == Items.CROSSBOW) &&
                (!MComesToLife.getMainConfig().getBoolean("config.infinite-arrows", false)
                        || getInventoryHolder().hasAnyOf("ARROW")
                        || (getLivingEntity().getEquipment().getItemInMainHand().getType() == Material.CROSSBOW
                                && getInventoryHolder().hasAnyOf("ROCKET")));
    }

    public void reloadDefenseIA() {
        EntityAi.updateVillagerDefend(this.getMob());
    }

    public void performAttack(LivingEntity target) {
        float damage = ItemSerializer
                .getDamage(this.getLivingEntity().getEquipment().getItemInMainHand(),
                        BetterEntity.getNmsEntity(target));

        // org.bukkit.event.entity
        org.bukkit.event.entity.EntityDamageByEntityEvent event = new org.bukkit.event.entity.EntityDamageByEntityEvent(
                this.getLivingEntity(), target, org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                damage);

        event.callEvent();
        if (event.isCancelled()) {
            return;
        }
        damage = (float) event.getDamage();
        BetterEntity targetE = new BetterEntity(target);
        getNMSEntity().swing(net.minecraft.world.InteractionHand.MAIN_HAND);
        // targetE.getNMSEntity().hurt(DamageSource.mobAttack(getNMSEntity()), damage);
        if (targetE.getNMSEntity().getHealth() - damage <= 0) {
            targetE.getNMSEntity().die(DamageSource.mobAttack(getNMSEntity()));
        } else {
            targetE.getNMSEntity().setHealth(targetE.getNMSEntity().getMaxHealth() - damage);
        }
        this.putContainerData(CustomMemory.TARGET_UUID, targetE.getLivingEntity().getUniqueId().toString());
    }

    public void releaseUsingItem(Version version) {
        if (version == Version.v1_19_R1) {
            MComesToLife.getNms().forceMethod(this.getNMSEntity(), "eR");
            return;
        } else {
            MComesToLife.getNms().forceMethod(this.getNMSEntity(), "releaseUsingItem");
        }
    }
}