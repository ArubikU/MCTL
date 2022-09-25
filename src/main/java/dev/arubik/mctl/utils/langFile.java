package dev.arubik.mctl.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class langFile {
    @Getter
    private FileConfiguration lang;

    public langFile(String path) {
        lang = fileUtils.getFileConfiguration(path);
    }

    public String getLang(String path) {
        return lang.getConfig().getString(path);
    }

    public String getLang(String path, String value) {
        return lang.getString(path, value);
    }

    public String getLang(String[] path) {
        return lang.getString(path, "No lang avaliable {" + path + "}");
    }

    public String getLang(String[] path, String value) {
        return lang.getString(path, value);
    }

    public List<String> getLangList(String path) {
        return lang.getConfig().getStringList(path);
    }

    public List<String> getLangList(String path, List<String> value) {
        return lang.getStringList(path, value);
    }

    public List<String> getLangList(String[] path) {
        return lang.getStringList(path, new ArrayList<String>());
    }

    public List<String> getLangList(String[] path, List<String> value) {
        return lang.getStringList(path, value);
    }
}
