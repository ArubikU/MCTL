package dev.arubik.mctl.utils;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.Json.LineConfig;
import me.clip.placeholderapi.PlaceholderAPI;

public class ConditionReader {
    private LineConfig line;
    private String realline;

    public ConditionReader(String line) {
        this.realline = line;
        this.line = new LineConfig(realline);
    }

    public String getLine() {
        return realline;
    }

    public void redoConditions(Player p) {

        if (MComesToLife.getEnabledPlugins().isEnabled("PlaceholderAPI")) {
            realline = PlaceholderAPI.setPlaceholders(p, realline);
        }

        Message message = new Message(realline);
        if (DataMethods.getSex(p) != null) {
            message.ContraryformatSex(DataMethods.getSex(p));
            message.formatPlayerSex(DataMethods.getSex(p));
        }
        realline = message.getString();
        if (DataMethods.getlastClickedEntity(p) != null) {
            message.formatSex(DataMethods.getlastClickedEntity(p).getSex());
            realline = message.getString();
            realline = MessageUtils.replace(DataMethods.getlastClickedEntity(p), p, realline);
        }
        line = new LineConfig(realline);
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
        if (MComesToLife.isDEBUG()) {
            MessageUtils.BukkitLog("Condition Type: " + line.getString("condition", "any"));
        }
        return Conditions.contains(line.getString("condition", "any"));
    }

    public Boolean checkCondition(Player player, ConfigurationSection section) {

        boolean and = true;
        boolean or = true;

        if (line.getString("ANDcondition", "any") != "any") {
            if (section.contains(line.getString("&&condition", "any"))) {
                ConditionReader cond = new ConditionReader(section.getString(line.getString("&&condition", "any")));
                cond.redoConditions(player);
                and = cond.checkCondition(player, section);
            }
        }
        if (line.getString("ORcondition", "any") != "any") {
            if (section.contains(line.getString("ORcondition", "any"))) {
                ConditionReader cond = new ConditionReader(section.getString(line.getString("&&condition", "any")));
                cond.redoConditions(player);
                or = cond.checkCondition(player, section);
            }
        }

        if (line.getBoolean("invert", false))
            return !((preCondition(line.getString("condition", "any").toUpperCase(),
                    line.getString("second_value", "null"), player) && and) || or);
        return ((preCondition(line.getString("condition", "any").toUpperCase(),
                line.getString("second_value", "null"), player) && and) || or);
    }

    private Boolean preCondition(String condition, String arg1, Player p) {
        String value = line.getString("value");
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
            return DataMethods.isSon(DataMethods.getlastClickedEntity(p).getLivingEntity(), p);
        } else if (condition.equals(Conditions.NOT_IS_SON.toString())) {
            return !DataMethods.isSon(DataMethods.getlastClickedEntity(p).getLivingEntity(), p);
        } else if (condition.equals(Conditions.IS_SPOUSE.toString())) {
            return DataMethods.getlastClickedEntity(p).getSpouse().equalsIgnoreCase(p.getUniqueId().toString());
        } else if (condition.equals(Conditions.NOT_IS_SPOUSE.toString())) {
            return !DataMethods.getlastClickedEntity(p).getSpouse().equalsIgnoreCase(p.getUniqueId().toString());
        } else if (condition.equals(Conditions.PERMISSION.toString())) {
            return p.hasPermission(value);
        }
        return false;
    }
}
