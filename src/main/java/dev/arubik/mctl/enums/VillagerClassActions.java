package dev.arubik.mctl.enums;

import org.bukkit.entity.Player;

import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.utils.NumberUtils;

public enum VillagerClassActions {
    GIVELIKES,
    TAKELIKES,
    GIVEHEALTH,
    SETMOOD,
    REBOOT;

    public void run(CustomVillager vil, Player interacter, Object extraData) {
        switch (this) {
            case GIVELIKES: {
                if (NumberUtils.isCreatable(extraData.toString())) {
                    int likes = Integer.parseInt(extraData.toString());
                    vil.addLikes(likes, interacter);
                }
                break;
            }
            case TAKELIKES: {
                if (NumberUtils.isCreatable(extraData.toString())) {
                    int likes = Integer.parseInt(extraData.toString());
                    vil.takeLikes(likes, interacter);
                }
                break;
            }
            case GIVEHEALTH: {
                if (NumberUtils.isCreatable(extraData.toString())) {
                    int health = Integer.parseInt(extraData.toString());
                    vil.regen(health);
                }
                break;
            }
            case SETMOOD: {
                if (NumberUtils.isCreatable(extraData.toString())) {
                    if (Mood.contains(extraData.toString())) {
                        Mood mood = Mood.valueOf(extraData.toString());
                        vil.setMood(mood);
                    }
                }
                break;
            }
            case REBOOT: {
                vil.removeData();
                vil = new CustomVillager(vil.getLivingEntity());
                vil.loadVillager(true);
                break;
            }
        }
    }

    public static Boolean contains(String arg) {
        for (TypeAction arg0 : TypeAction.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    public static VillagerClassActions getAction(String arg) {
        for (VillagerClassActions arg0 : VillagerClassActions.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return arg0;
            }
        }
        return null;
    }
}
