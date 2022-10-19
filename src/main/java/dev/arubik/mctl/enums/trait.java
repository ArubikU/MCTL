package dev.arubik.mctl.enums;

public enum Trait {
    SHY,
    FUN,
    SERIOUS,
    FRIENDLY,
    IRRITABLE,
    EMOTIONAL,
    OUTGOING;

    public static Boolean contains(String arg) {
        for (Trait arg0 : Trait.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }
}
