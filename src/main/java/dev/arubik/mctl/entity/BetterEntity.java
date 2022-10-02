package dev.arubik.mctl.entity;

import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.holders.EntityAi;
import dev.arubik.mctl.holders.Nms;
import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.ItemSerializer;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.memories.EntityMemory;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
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

    public void regen() {
        regen(DataMethods.rand(0, 6));
    }

    @Override
    public PlayerDisguise generateDisguise() {
        return null;
    }

    @Override
    public void Disguise() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Disguise(Player... p) {
        // TODO Auto-generated method stub

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
        if (!MComesToLife.config.getBoolean("config.infinite-arrows", false)) {
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
            org.bukkit.inventory.ItemStack ba = this.getInventoryHolder().consumeItem("ARROW");
            if (ba != null) {
                arrowA = ba;
                ItemStack temp = MComesToLife.getNms().castMethod(
                        "org.bukkit.craftbukkit.{version}.inventory.CraftItemStack",
                        "asNMSCopy", ItemStack.class, ba);
                if (temp != null) {
                    arrow = temp;
                }
            }
        }

        ArrowItem arrowAsItem = (ArrowItem) (arrow.getItem() instanceof ArrowItem ? arrow.getItem() : Items.ARROW);

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
        // MComesToLife.config.getBoolean("config.infinite-arrows", false)
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
            if(event.getProjectile()!=null){
                if(!event.getProjectile().isDead()){
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
        if (this.getMemory(EntityMemory.ATTACK_TARGET) == null) {
            return null;
        }
        return new BetterEntity(this.getMemory(EntityMemory.ATTACK_TARGET)).getNMSEntity();
    }

    @Override
    public void onCrossbowAttackPerformed() {
        // TODO Auto-generated method stub

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
                (!MComesToLife.config.getBoolean("config.infinite-arrows", false)
                        || getInventoryHolder().hasAnyOf("ARROW"));
    }

    public void test() {
    }

    public void reloadDefenseIA() {
        EntityAi.updateVillagerDefend(this.getMob());
    }
}