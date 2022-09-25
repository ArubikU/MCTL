package dev.arubik.mctl.enums;

public enum TypeAction {
    STORY,
    CHAT,
    JOKE,
    PLAY,
    GREET,
    FLIRT,
    KISS,
    INSULT,
    FOLLOW,
    STOP,
    STAY,
    SETHOME,
    GOHOME,
    GIFT,
    PROCREATE,
    UNEQUIPHAT,
    UNEQUIPCHEST,
    UNEQUIPLEGS,
    UNEQUIPBOOTS,
    UNEQUIPHAND,
    UNEQUIPOFFHAND;

    public static Boolean contains(String arg) {
        for (TypeAction arg0 : TypeAction.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }
}
