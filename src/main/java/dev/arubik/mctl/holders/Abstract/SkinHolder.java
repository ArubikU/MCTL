package dev.arubik.mctl.holders.Abstract;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.mineskin.MineskinClient;
import org.mineskin.SkinOptions;
import org.mineskin.Variant;
import org.mineskin.Visibility;
import org.mineskin.data.SkinData;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.utils.FileConfiguration;
import dev.arubik.mctl.utils.fileUtils;
import dev.arubik.mctl.utils.messageUtils;
import dev.arubik.mctl.utils.MultiThings.BiObject;
import dev.arubik.mctl.utils.MultiThings.MultiObjects.TriObject;
import me.libraryaddict.disguise.utilities.SkinUtils;
import me.libraryaddict.disguise.utilities.mineskin.MineSkinResponse;
import me.libraryaddict.disguise.utilities.translations.LibsMsg;
import net.skinsrestorer.api.SkinVariant;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import net.skinsrestorer.api.interfaces.IPropertyFactory;
import net.skinsrestorer.api.property.IProperty;
import net.skinsrestorer.shared.utils.connections.MineSkinAPI;

public class SkinHolder {

    public void preLoadSkins(List<String> skins) {
        skins.forEach(skin -> {
            SkinVariant variant = SkinVariant.CLASSIC;
            if (skin.startsWith("slim:") || skin.startsWith("alex:")) {
                skin = skin.replace("slim:", "").replace("alex:", "");
                variant = SkinVariant.SLIM;
            }
            if (skin.startsWith("normal:") || skin.startsWith("steve:")) {
                skin = skin.replace("normal:", "").replace("steve:", "");
                variant = SkinVariant.CLASSIC;
            }
            if (fileUtils.getFile(skin.replace(".png", ".yaml")).exists()) {
                messageUtils.Bukkitlog("[MCTL] Skin" + skin + " already exists, Skiped generation");
                return;
            }

            try {
                genSkinFromFile(skin, variant);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                    | IOException e) {
                e.printStackTrace();
            }
        });
    }

    public WrappedGameProfile getSkinFromUrl(String url, SkinVariant vr, WrappedGameProfile profile)
            throws SkinRequestException {
        if (MComesToLife.enabledPlugins.isEnabled("SkinsRestorer")) {
            // get Skin from url using SkinsRestorer
            SkinsRestorerAPI api = SkinsRestorerAPI.getApi();
            IProperty skin = api.genSkinUrl(url, vr);
            profile.getProperties().put(IProperty.TEXTURES_NAME,
                    new WrappedSignedProperty(skin.getName(), skin.getValue(), skin.getSignature()));

        }
        return profile;
    }

    public WrappedGameProfile genSkinFromData(String value, String signature, WrappedGameProfile profile) {
        if (MComesToLife.enabledPlugins.isEnabled("SkinsRestorer")) {
            profile.getProperties().put(IProperty.TEXTURES_NAME,
                    // get Skin from url using SkinsRestorer
                    new WrappedSignedProperty(profile.getName(), value, signature));
        }
        return profile;
    }

    public WrappedGameProfile getSkinFromName(String name, SkinVariant vr, WrappedGameProfile profile)
            throws SkinRequestException {
        if (MComesToLife.enabledPlugins.isEnabled("SkinsRestorer")) {
            // get Skin from url using SkinsRestorer
            SkinsRestorerAPI api = SkinsRestorerAPI.getApi();
            IProperty skin = api.getSkinData(name);
            profile.getProperties().put(IProperty.TEXTURES_NAME,
                    new WrappedSignedProperty(skin.getName(), skin.getValue(), skin.getSignature()));

        }
        return profile;
    }

    public WrappedGameProfile getSkinFromPath(String path, SkinVariant vr, WrappedGameProfile profile) {
        String yamlPath = path.replace(".png", ".yaml");
        if (!fileUtils.getFile(path).exists())
            return profile;
        if (!fileUtils.getFile(yamlPath).exists()) {
            try {
                genSkinFromFile(path, vr);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                    | IOException e) {
                e.printStackTrace();
            }
            messageUtils.Bukkitlog("[MCTL] Please reload to re apply the skin from files");
            return profile;
        } else {
            FileConfiguration file = fileUtils.getFileConfiguration(yamlPath);
            profile.getProperties().put(IProperty.TEXTURES_NAME,
                    new WrappedSignedProperty(file.getString("name", profile.getName()),
                            file.getString("value", profile.getId()),
                            file.getString("signature", profile.getUUID().toString())));
        }

        return profile;
    }

    public WrappedGameProfile getSkinFromPath(String path, SkinVariant vr, WrappedGameProfile profile,
            CustomVillager vi) {
        String yamlPath = path.replace(".png", ".yaml");
        if (!fileUtils.getFile(path).exists())
            return profile;
        if (!fileUtils.getFile(yamlPath).exists()) {
            try {
                genSkinFromFile(path, vr, vi);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                    | IOException e) {
                e.printStackTrace();
            }
            messageUtils.Bukkitlog("[MCTL] Please reload to re apply the skin from files");
            return profile;
        } else {
            FileConfiguration file = fileUtils.getFileConfiguration(yamlPath);
            profile.getProperties().put(IProperty.TEXTURES_NAME,
                    new WrappedSignedProperty(file.getString("name", profile.getName()),
                            file.getString("value", profile.getId()),
                            file.getString("signature", profile.getUUID().toString())));
        }

        return profile;
    }

    public void genSkinFromFile(String path, SkinVariant vr)
            throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        if (MComesToLife.enabledPlugins.isEnabled("SkinsRestorer")) {
            final String value = "";
            File f = new File(MComesToLife.getPlugin().getDataFolder(), path);
            MineskinClient client = new MineskinClient("MCTL",
                    "924cbd9601658e92f0234cae06162fd9c582237f0cf3706d00775d045e7cdc1b");
            client.generateUpload(f, SkinOptions.create(path, Variant.AUTO, Visibility.PUBLIC)).thenAccept(Skin -> {

                messageUtils.Bukkitlog("Saved skin file: " + path);
                // WrappedSignedProperty property = arg0.getProperties()
                // .get(IProperty.TEXTURES_NAME).iterator().next();

                String yamlPath = path.replace(".png", ".yaml");
                FileConfiguration saved = fileUtils.getFileConfiguration(yamlPath);
                // saved.set("variant", vr);
                saved.set("value", Skin.data.texture.value);
                saved.set("signature", Skin.data.texture.signature);
                saved.set("name", Skin.name);
                saved.save();
            });
        }
    }

    public void genSkinFromFile(String path, SkinVariant vr, CustomVillager vi)
            throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        if (MComesToLife.enabledPlugins.isEnabled("SkinsRestorer")) {
            final String value = "";
            File f = new File(MComesToLife.getPlugin().getDataFolder(), path);
            MineskinClient client = new MineskinClient("MCTL",
                    "924cbd9601658e92f0234cae06162fd9c582237f0cf3706d00775d045e7cdc1b");
            client.generateUpload(f, SkinOptions.create(path, Variant.AUTO, Visibility.PUBLIC)).thenAccept(Skin -> {

                messageUtils.Bukkitlog("Saved skin file: " + path);
                // WrappedSignedProperty property = arg0.getProperties()
                // .get(IProperty.TEXTURES_NAME).iterator().next();

                String yamlPath = path.replace(".png", ".yaml");
                FileConfiguration saved = fileUtils.getFileConfiguration(yamlPath);
                // saved.set("variant", vr);
                saved.set("value", Skin.data.texture.value);
                saved.set("signature", Skin.data.texture.signature);
                saved.set("name", Skin.name);
                saved.save();

                vi.Disguise();
            });
        }
    }
}
