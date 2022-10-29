package dev.arubik.mctl.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import dev.arubik.mctl.MComesToLife;

public class FileUtils {

    public static FileConfiguration getFileConfiguration(String path) {
        if (path == "config.yml") {
            return MComesToLife.getMainConfig();
        }
        File f = new File(MComesToLife.getPlugin().getDataFolder(), path);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
            org.bukkit.configuration.file.FileConfiguration data = (org.bukkit.configuration.file.FileConfiguration) s;
            data.options().copyDefaults(true);
            s.options().copyDefaults(true);
            try {
                try {
                    if (MComesToLife.getPlugin().getResource(path) != null && path != "data.yml") {
                        MComesToLife.getPlugin().saveResource(path, false);
                    }
                } catch (java.lang.IllegalArgumentException e) {
                    MessageUtils.log("<red>MCTL: <white>Failed to load default file <yellow>" + path);
                }
                data.save(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new FileConfiguration(data, path);
        }
        YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
        org.bukkit.configuration.file.FileConfiguration data = (org.bukkit.configuration.file.FileConfiguration) s;
        return new FileConfiguration(data, path);
    }

    public static File getFile(String path) {
        return new File(MComesToLife.getPlugin().getDataFolder(), path);
    }

    public static void saveFile(org.bukkit.configuration.file.FileConfiguration file, String path) {
        File f = new File(MComesToLife.getPlugin().getDataFolder(), path);
        try {
            file.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Boolean getBoolean(String file, String path) {
        return FileUtils.getFileConfiguration(file).getConfig().getBoolean(path);
    }

    public static Boolean getBoolean(String file, String path, Boolean value) {
        return FileUtils.getFileConfiguration(file).getBoolean(path, value);
    }

    public static Boolean getBoolean(String file, String[] path, Boolean value) {
        return FileUtils.getFileConfiguration(file).getBoolean(path, value);
    }

    public static String getString(String file, String path) {
        return FileUtils.getFileConfiguration(file).getConfig().getString(path);
    }

    public static String getString(String file, String path, String value) {
        return FileUtils.getFileConfiguration(file).getString(path, value);
    }

    public static String getString(String file, String[] path, String value) {
        return FileUtils.getFileConfiguration(file).getString(path, value);
    }
}
