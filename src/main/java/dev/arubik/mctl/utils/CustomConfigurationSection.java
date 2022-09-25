package dev.arubik.mctl.utils;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataType;

public class CustomConfigurationSection {
    private ConfigurationSection sec;

    public CustomConfigurationSection(ConfigurationSection section) {
        this.sec = section;
    }

    public boolean getBoolean(String path, boolean value) {
        if (sec.contains(path)) {
            return sec.getBoolean(path);
        }
        return value;
    }

    public boolean getBoolean(String[] path, boolean value) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                return sec.getBoolean(pathPart);
            }
        }
        return value;
    }

    public String getString(String path, String value) {
        if (sec.contains(path)) {
            return sec.getString(path);
        }
        return value;
    }

    public String getString(String[] path, String value) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                return sec.getString(pathPart);
            }
        }
        return value;
    }

    public List<String> getStringList(String path, List<String> value) {
        if (sec.contains(path)) {
            return sec.getStringList(path);
        }
        return value;
    }

    public List<String> getStringList(String[] path, List<String> value) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                return sec.getStringList(pathPart);
            }
        }
        return value;
    }

    public int getInt(String path, int value) {
        if (sec.contains(path)) {
            return sec.getInt(path);
        }
        return value;
    }

    public int getInt(String[] path, int value) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                return sec.getInt(pathPart);
            }
        }
        return value;
    }

    public Double getDouble(String path, Double value) {
        if (sec.contains(path)) {
            return sec.getDouble(path);
        }
        return value;
    }

    public Double getDouble(String[] path, Double value) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                return sec.getDouble(pathPart);
            }
        }
        return value;
    }

    public Integer getInteger(String path, Integer value) {
        if (sec.contains(path)) {
            return sec.getInt(path);
        }
        return value;
    }

    public Integer getInteger(String[] path, Integer value) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                return sec.getInt(pathPart);
            }
        }
        return value;
    }

    public ConfigurationSection getConfigurationSection(String path, ConfigurationSection value) {
        if (sec.contains(path)) {
            return sec.getConfigurationSection(path);
        }
        return value;
    }

    public ConfigurationSection getConfigurationSection(String[] path, ConfigurationSection value) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                return sec.getConfigurationSection(pathPart);
            }
        }
        return value;
    }

    public Boolean contains(String... path) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                return true;
            }
        }
        return false;
    }

    public Object get(String path, String value) {
        if (sec.contains(path)) {
            return sec.get(path);
        }
        return value;
    }

    public Object get(String[] path, String value) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                return sec.get(pathPart);
            }
        }
        return value;
    }

    public PersistentDataType getType(String... path) {
        for (String pathPart : path) {
            if (sec.contains(pathPart)) {
                if (sec.get(pathPart) instanceof Integer) {
                    return PersistentDataType.INTEGER;
                } else if (sec.get(pathPart) instanceof Double) {
                    return PersistentDataType.DOUBLE;
                } else if (sec.get(pathPart) instanceof String) {
                    return PersistentDataType.STRING;
                } else if (sec.get(pathPart) instanceof Boolean) {
                    return PersistentDataType.BYTE;
                } else if (sec.get(pathPart) instanceof Long) {
                    return PersistentDataType.LONG;
                } else if (sec.get(pathPart) instanceof Float) {
                    return PersistentDataType.FLOAT;
                } else if (sec.get(pathPart) instanceof Short) {
                    return PersistentDataType.SHORT;
                } else if (sec.get(pathPart) instanceof Byte) {
                    return PersistentDataType.BYTE_ARRAY;
                } else if (sec.get(pathPart) instanceof Character) {
                    return PersistentDataType.BYTE;
                }
            }
        }
        return PersistentDataType.STRING;
    }

}
