package dev.arubik.mctl.utils;

public class NumberUtils {
    public static Boolean isCreatable(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
