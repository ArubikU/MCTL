package dev.arubik.mctl.entity;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import dev.arubik.mctl.holders.Methods.DataMethods;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class BetterEntity extends CustomEntity implements EntityMethods {
    public BetterEntity(LivingEntity v) {
        super(v);
    }

    public void regen() {
        regen(DataMethods.rand(0, 6));
    }

    public LivingEntity getLivingEntity() {
        return (LivingEntity) villager;
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
}