package dev.arubik.mctl.enums;

public enum sex {
    male, female;

    public static Boolean contains(String arg) {
        for (sex arg0 : sex.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }
}
