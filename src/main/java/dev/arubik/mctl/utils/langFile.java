package dev.arubik.mctl.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class LangFile {
    @Getter
    private FileConfiguration lang;

    public LangFile(String path) {
        lang = FileUtils.getFileConfiguration(path);
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
