package dev.arubik.mctl.enums;

public enum trait {
    SHY,
    FUN,
    SERIOUS,
    FRIENDLY,
    IRRITABLE,
    EMOTIONAL,
    OUTGOING;

    public static Boolean contains(String arg) {
        for (trait arg0 : trait.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }
}
