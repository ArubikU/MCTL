package dev.arubik.mctl.holders.Skins;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.Mood;
import dev.arubik.mctl.enums.Sex;
import dev.arubik.mctl.utils.CustomConfigurationSection;
import dev.arubik.mctl.utils.MultiThings.BiObject;
import io.lumine.mythic.bukkit.utils.files.Folders;

public class SkinGenerator {
    private HashMap<String, String> MaleMap = new HashMap<String, String>();
    private HashMap<String, String> FemaleMap = new HashMap<String, String>();
    private List<String> MaleSkinColors = new ArrayList<String>();
    private List<String> FemaleSkinColors = new ArrayList<String>();
    private List<String> HairColors = new ArrayList<String>();

    public SkinGenerator(CustomConfigurationSection config) {
        MaleMap.put("skin", config.getString("male.skin-path", "skins/skin/male"));
        MaleMap.put("hair", config.getString("make.hair-path", "skins/hair/male"));
        MaleMap.put("face", config.getString("make.face-path", "skins/face/normal/male"));
        MaleMap.put("clothing", config.getString("make.clothing-path", "skins/clothing/normal/male/{profession}"));

        FemaleMap.put("skin", config.getString("female.skin-path", "skins/skin/female"));
        FemaleMap.put("hair", config.getString("female.hair-path", "skins/hair/female"));
        FemaleMap.put("face", config.getString("female.face-path", "skins/face/normal/female"));
        FemaleMap.put("clothing",
                config.getString("female.clothing-path", "skins/clothing/normal/female/{profession}"));
    }

    public String generateSkin(CustomVillager villager) throws IOException {
        String path = "";
        HashMap<String, String> DataMap = (HashMap<String, String>) MaleMap.clone();
        List<String> ColorMap = MaleSkinColors;
        if (villager.getSex() == Sex.female) {
            DataMap = (HashMap<String, String>) FemaleMap.clone();
            ColorMap = FemaleSkinColors;
        }
        // get random skin from folder of DataMap with key "skin"
        // generate a path
        Path skinPath = Path.of(MComesToLife.getPlugin().getDataFolder().getPath(), DataMap.get("skin").toString());
        Stream<Path> paths = java.nio.file.Files.list(skinPath);
        int random = Mood.rand(0, paths.toArray().length);
        // png file
        String skin = paths.toArray()[random].toString();
        File skinFile = new File(skin);

        // java.nio.file.Files.list(Path.)
        return path;
    }
}
