package dev.arubik.mctl.utils;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

public class FileConfiguration {

    @Getter
    @Setter
    private org.bukkit.configuration.file.FileConfiguration config;
    private String relative;

    public FileConfiguration(org.bukkit.configuration.file.FileConfiguration con, String path) {
        this.config = con;
        this.relative = path;
    }

    public void save() {
        fileUtils.saveFile(config, relative);
    }

    public <T> T set(String path, T value) {
        config.set(path, value);
        return value;
    }

    public boolean getBoolean(String path, boolean value) {
        if (config.contains(path)) {
            return config.getBoolean(path);
        }
        config.set(path, value);
        fileUtils.saveFile(config, relative);
        return value;
    }

    public boolean getBoolean(String[] path, boolean value) {
        for (String pathPart : path) {
            if (config.contains(pathPart)) {
                return config.getBoolean(pathPart);
            }
        }
        config.set(path[path.length - 1], value);
        return value;
    }

    public String getString(String path, String value) {
        if (config.contains(path)) {
            return config.getString(path);
        }
        config.set(path, value);
        fileUtils.saveFile(config, relative);
        return value;
    }

    public String getString(String[] path, String value) {
        for (String pathPart : path) {
            if (config.contains(pathPart)) {
                return config.getString(pathPart);
            }
        }
        config.set(path[path.length - 1], value);
        return value;
    }

    public List<String> getStringList(String path, List<String> value) {
        if (config.contains(path)) {
            return config.getStringList(path);
        }
        config.set(path, value);
        fileUtils.saveFile(config, relative);
        return value;
    }

    public List<String> getStringList(String[] path, List<String> value) {
        for (String pathPart : path) {
            if (config.contains(pathPart)) {
                return config.getStringList(pathPart);
            }
        }
        config.set(path[path.length - 1], value);
        return value;
    }

    public int getInt(String path, int value) {
        if (config.contains(path)) {
            return config.getInt(path);
        }
        config.set(path, value);
        fileUtils.saveFile(config, relative);
        return value;
    }

    public int getInt(String[] path, int value) {
        for (String pathPart : path) {
            if (config.contains(pathPart)) {
                return config.getInt(pathPart);
            }
        }
        config.set(path[path.length - 1], value);
        return value;
    }

    public Double getDouble(String path, Double value) {
        if (config.contains(path)) {
            return config.getDouble(path);
        }
        config.set(path, value);
        fileUtils.saveFile(config, relative);
        return value;
    }

    public Double getDouble(String[] path, Double value) {
        for (String pathPart : path) {
            if (config.contains(pathPart)) {
                return config.getDouble(pathPart);
            }
        }
        config.set(path[path.length - 1], value);
        return value;
    }

    public Integer getInteger(String path, Integer value) {
        if (config.contains(path)) {
            return config.getInt(path);
        }
        config.set(path, value);
        fileUtils.saveFile(config, relative);
        return value;
    }

    public Integer getInteger(String[] path, Integer value) {
        for (String pathPart : path) {
            if (config.contains(pathPart)) {
                return config.getInt(pathPart);
            }
        }
        config.set(path[path.length - 1], value);
        return value;
    }

    public ConfigurationSection getConfigurationSection(String path, ConfigurationSection value) {
        if (config.contains(path)) {
            return config.getConfigurationSection(path);
        }
        config.set(path, value);
        fileUtils.saveFile(config, relative);
        return value;
    }

    public ConfigurationSection getConfigurationSection(String[] path, ConfigurationSection value) {
        for (String pathPart : path) {
            if (config.contains(pathPart)) {
                return config.getConfigurationSection(pathPart);
            }
        }
        config.set(path[path.length - 1], value);
        return value;
    }

    public void loadFromString(@NotNull String contents) throws InvalidConfigurationException {
        config.loadFromString(contents);
    }

    public Boolean Contains(@NotNull String... path){
        for(String subPath : path){
            if(config.contains(subPath)){return true;}
        }
        return false;
    }

}
