package dev.arubik.mctl.entity;

import org.bukkit.entity.LivingEntity;

import me.libraryaddict.disguise.DisguiseAPI;

public class DisguisedEntity {
    LivingEntity disguised;
    me.libraryaddict.disguise.disguisetypes.Disguise disguise;
    public DisguisedEntity(LivingEntity v) {
        disguised = v;
        disguise = DisguiseAPI.getDisguise(disguised);
    }

    public void setUsingOffHand(boolean b) {
    }
}
