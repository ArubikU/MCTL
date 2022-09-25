package dev.arubik.mctl.holders;

import java.util.HashMap;

import org.bukkit.entity.Player;

import dev.arubik.mctl.utils.CustomConfigurationSection;
import dev.arubik.mctl.utils.fileUtils;

public class WaitTimePerAction {
    String action;

    public WaitTimePerAction(String action) {
        this.action = action;
    }

    public HashMap<String, Long> timeMap = new HashMap<String, Long>();

    public void addToTime(Player p) {
        timeMap.put(p.getName(), System.currentTimeMillis());
    }

    public Boolean able(Player p, Long time) {
        if (timeMap.containsKey(p.getName())) {
            if (timeMap.get(p.getName()) + time < System.currentTimeMillis()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public Long timeNeed(Player p, Long time) {
        if (timeMap.containsKey(p.getName())) {
            if (timeMap.get(p.getName()) + time < System.currentTimeMillis()) {
                return 0L;
            } else {
                return timeMap.get(p.getName()) + time - System.currentTimeMillis();
            }
        } else {
            return 0L;
        }
    }

    public Boolean Enabled() {
        return fileUtils.getBoolean("config.yml", "config.wait-" + action, false);
    }

}
