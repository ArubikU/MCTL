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
        Bukkit.getScheduler().runTaskLater(MComesToLife.getPlugin(), e,0L);
    }
}
