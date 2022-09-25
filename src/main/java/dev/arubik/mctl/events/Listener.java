package dev.arubik.mctl.events;

import dev.arubik.mctl.MComesToLife;

public class Listener implements org.bukkit.event.Listener {
    protected static MComesToLife plugin = (MComesToLife) MComesToLife.getPlugin();
    public static String type = "BUKKIT";

    public Listener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregister() {
        org.bukkit.event.HandlerList.unregisterAll(this);
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}