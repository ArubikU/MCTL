package dev.arubik.mctl.holders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class EnabledPlugins {
    private HashMap<String, Boolean> enabledPlugins = new HashMap<String, Boolean>();
    private List<String> forceDisable = new ArrayList<String>();

    public EnabledPlugins() {
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            enabledPlugins.put(p.getName(), p.isEnabled());
        }
    }

    public void forceDisable(String pluginName) {
        forceDisable.add(pluginName);
    }

    public boolean isEnabled(String pluginName) {
        if (forceDisable.contains(pluginName))
            return false;
        if (enabledPlugins.containsKey(pluginName)) {
            return enabledPlugins.get(pluginName);
        } else {
            return Bukkit.getPluginManager().getPlugin(pluginName) != null;
        }
    }
}
