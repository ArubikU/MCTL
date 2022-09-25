package dev.arubik.mctl.holders;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class EnabledPlugins {
    private HashMap<String, Boolean> enabledPlugins = new HashMap<String, Boolean>();

    public EnabledPlugins() {
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            enabledPlugins.put(p.getName(), p.isEnabled());
        }
    }

    public boolean isEnabled(String pluginName) {
        if (enabledPlugins.containsKey(pluginName)) {
            return enabledPlugins.get(pluginName);
        } else {
            return Bukkit.getPluginManager().getPlugin(pluginName) != null;
        }
    }
}
