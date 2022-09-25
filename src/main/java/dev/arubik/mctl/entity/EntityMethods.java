package dev.arubik.mctl.entity;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;

import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public interface EntityMethods {
    PlayerDisguise generateDisguise();

    void Disguise();

    void Disguise(Player... p);

    void regen();

    void regen(int health);

    void addModifier(Attribute attribute, Double value, Operation op);
}
