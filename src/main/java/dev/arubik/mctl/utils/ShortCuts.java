package dev.arubik.mctl.utils;

import org.bukkit.Bukkit;

import dev.arubik.mctl.MComesToLife;

public class ShortCuts {
    public static LangFile getMessages() {
        return MComesToLife.getMessages();
    }

    public static void Async(Runnable e) {
        Bukkit.getScheduler().runTaskAsynchronously(MComesToLife.getPlugin(), e);
    }

    public static void Sync(Runnable e) {
        Bukkit.getScheduler().runTaskLater(MComesToLife.getPlugin(), e, 0L);
    }

    public static String getUniquePath(org.bukkit.entity.Entity e) {
        return "data/" + e.getUniqueId().toString() + ".yml";
    }

    public static FileConfiguration getFile(org.bukkit.entity.Entity e) {
        return FileUtils.getFileConfiguration(getUniquePath(e));
    }
}
