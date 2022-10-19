package dev.arubik.mctl.utils;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;

import dev.arubik.mctl.MComesToLife;

public class TimeUtils {

    public static Long getTimeOutProcreate() {
        ConfigurationSection conf = MComesToLife.getMainConfig().getConfig()
                .getConfigurationSection("config.procreate-timeout");
        int days = conf.getInt("days");
        int hours = conf.getInt("hours");
        int minutes = conf.getInt("minutes");
        int seconds = conf.getInt("seconds");
        // convert time to millis
        long time = (days * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
        return time;
    }

    public static Long getTimeFromConfig(CustomConfigurationSection conf) {
        int days = conf.getInt("days", 0);
        int hours = conf.getInt("hours", 0);
        int minutes = conf.getInt("minutes", 0);
        int seconds = conf.getInt("seconds", 0);
        // convert time to millis
        long time = (days * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
        return time;
    }

    public static Long getTimeFromArgs(int days, int hours, int minutes, int seconds) {
        long time = (days * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
        return time;
    }

    public static HashMap<TimeFormat, Integer> getTimeFromDate(long time) {
        HashMap<TimeFormat, Integer> timeMap = new HashMap<TimeFormat, Integer>();
        int days = (int) (time / (1000 * 60 * 60 * 24));
        int hours = (int) ((time / (1000 * 60 * 60)) % 24);
        int minutes = (int) ((time / (1000 * 60)) % 60);
        int seconds = (int) (time / 1000) % 60;
        timeMap.put(TimeFormat.DAY, days);
        timeMap.put(TimeFormat.HOURS, hours);
        timeMap.put(TimeFormat.MINUTES, minutes);
        timeMap.put(TimeFormat.SECONDS, seconds);
        return timeMap;
    }

}
