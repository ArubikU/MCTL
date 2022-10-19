package dev.arubik.mctl.utils;

import dev.arubik.mctl.holders.Methods.DataMethods;

public class NumberUtils {
    public static Boolean isCreatable(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Boolean isNaN(String string) {
        // is a number?
        if (!isCreatable(string))
            return false;
        return true;
    }

    public static Boolean probability(int percent) {
        return DataMethods.rand(0, 100) < percent;
    }
}
