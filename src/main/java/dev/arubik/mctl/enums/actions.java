package dev.arubik.mctl.enums;

public enum actions {
    open_interactions,
    open_home,
    trade;

    public static Boolean contains(String arg) {
        for (actions arg0 : actions.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }
}
