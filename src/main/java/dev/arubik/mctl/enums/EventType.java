package dev.arubik.mctl.enums;

public enum EventType {
    INTERACTION,
    VILLAGERDEAD,
    NULL;

    public static Boolean contains(String arg) {
        for (EventType arg0 : EventType.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    public static EventType getType(String arg) {
        for (EventType arg0 : EventType.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return arg0;
            }
        }
        return NULL;
    }
}