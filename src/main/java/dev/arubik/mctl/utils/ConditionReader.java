package dev.arubik.mctl.utils;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.Json.LineConfig;
import me.clip.placeholderapi.PlaceholderAPI;

public class ConditionReader {
    private LineConfig line;

    public ConditionReader(String line) {
        this.line = new LineConfig(line);
    }

    public enum Conditions {
        EQUALS,
        NOT_EQUALS,
        EQUALS_IGNORE_CASE,
        NOT_EQUALS_IGNORE_CASE,
        GREATER,
        LESS,
        GREATER_OR_EQUALS,
        LESS_OR_EQUALS,
        CONTAINS,
        NOT_CONTAINS,
        STARTS_WITH,
        NOT_STARTS_WITH,
        ENDS_WITH,
        NOT_ENDS_WITH,
        IS_SON,
        NOT_IS_SON,
        IS_SPOUSE,
        NOT_IS_SPOUSE,
        PERMISSION;

        public static Boolean contains(String arg) {
            for (Conditions arg0 : Conditions.values()) {
                if (arg0.toString().equalsIgnoreCase(arg)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Nullable
    public String get(String key) {
        return line.getString(key);
    }

    public Boolean isAvaliableCondition() {
        return Conditions.contains(line.getString("condition", "any"));
    }

    public Boolean checkCondition(Player player) {
        if (line.getBoolean("invert", false))
            return !preCondition(line.getString("condition", "any"), line.getString("second_value", "null"), player);
        return preCondition(line.getString("condition", "any"), line.getString("second_value", "null"), player);
    }

    private Boolean preCondition(String condition, String value, Player p) {
        String arg1 = line.getString("value");
        if (MComesToLife.getEnabledPlugins().isEnabled("PlaceholderAPI")) {
            arg1 = PlaceholderAPI.setPlaceholders(p, value);
        }
        if (condition.equals(Conditions.EQUALS.toString())) {
            return arg1.equals(value);
        } else if (condition.equals(Conditions.NOT_EQUALS.toString())) {
            return !arg1.equals(value);
        } else if (condition.equals(Conditions.EQUALS_IGNORE_CASE.toString())) {
            return arg1.equalsIgnoreCase(value);
        } else if (condition.equals(Conditions.NOT_EQUALS_IGNORE_CASE.toString())) {
            return !arg1.equalsIgnoreCase(value);
        } else if (condition.equals(Conditions.GREATER.toString())) {
            return NumberUtils.isCreatable(arg1) && NumberUtils.isCreatable(value)
                    && Double.parseDouble(value) > Double.parseDouble(arg1);
        } else if (condition.equals(Conditions.LESS.toString())) {
            return NumberUtils.isCreatable(arg1) && NumberUtils.isCreatable(value)
                    && Double.parseDouble(value) < Double.parseDouble(arg1);
        } else if (condition.equals(Conditions.GREATER_OR_EQUALS.toString())) {
            return NumberUtils.isCreatable(arg1) && NumberUtils.isCreatable(value)
                    && Double.parseDouble(value) >= Double.parseDouble(arg1);
        } else if (condition.equals(Conditions.LESS_OR_EQUALS.toString())) {
            return NumberUtils.isCreatable(arg1) && NumberUtils.isCreatable(value)
                    && Double.parseDouble(value) <= Double.parseDouble(arg1);
        } else if (condition.equals(Conditions.CONTAINS.toString())) {
            return value.contains(arg1);
        } else if (condition.equals(Conditions.NOT_CONTAINS.toString())) {
            return !value.contains(arg1);
        } else if (condition.equals(Conditions.STARTS_WITH.toString())) {
            return value.startsWith(arg1);
        } else if (condition.equals(Conditions.NOT_STARTS_WITH.toString())) {
            return !value.startsWith(arg1);
        } else if (condition.equals(Conditions.ENDS_WITH.toString())) {
            return value.endsWith(arg1);
        } else if (condition.equals(Conditions.NOT_ENDS_WITH.toString())) {
            return !value.endsWith(arg1);
        } else if (condition.equals(Conditions.IS_SON.toString())) {
            return DataMethods.isSon(MComesToLife.getlastClickedEntity(p).getLivingEntity(), p);
        } else if (condition.equals(Conditions.NOT_IS_SON.toString())) {
            return !DataMethods.isSon(MComesToLife.getlastClickedEntity(p).getLivingEntity(), p);
        } else if (condition.equals(Conditions.IS_SPOUSE.toString())) {
            return MComesToLife.getlastClickedEntity(p).getSpouse().equalsIgnoreCase(p.getUniqueId().toString());
        } else if (condition.equals(Conditions.NOT_IS_SPOUSE.toString())) {
            return !MComesToLife.getlastClickedEntity(p).getSpouse().equalsIgnoreCase(p.getUniqueId().toString());
        } else if (condition.equals(Conditions.PERMISSION.toString())) {
            return p.hasPermission(value);
        }
        return false;
    }
}
