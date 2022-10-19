package dev.arubik.mctl.enums;

public enum Sex {
    male, female;

    public static Boolean contains(String arg) {
        for (Sex arg0 : Sex.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }
}
