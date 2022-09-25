package dev.arubik.mctl.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.citizensnpcs.api.CitizensAPI;
import net.skinsrestorer.api.SkinVariant;
import net.skinsrestorer.api.exception.SkinRequestException;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.enums.Works;
import dev.arubik.mctl.enums.mood;
import dev.arubik.mctl.enums.sex;
import dev.arubik.mctl.enums.trait;
import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.holders.timers;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.FileConfiguration;
import dev.arubik.mctl.utils.fileUtils;
import dev.arubik.mctl.utils.messageUtils;
import dev.arubik.mctl.utils.Json.LineConfig;

public class CustomVillager extends BetterEntity {

    public CustomVillager(LivingEntity v) {
        super(v);
    }

    public Optional<Integer> getLikes(LivingEntity e) {
        return Optional.ofNullable((Integer) data.get("likes-" + e.getUniqueId().toString()));
    }

    public Optional<Integer> getHappiness() {
        return Optional.ofNullable((Integer) data.get("happiness"));
    }

    public void setHappiness(Integer i) {
        data.put("happiness", i);
        save();
    }

    public void addHappiness(Integer i) {
        data.put("happiness", Optional.ofNullable((Integer) data.get("happiness")).orElse(0) + i);
        save();
    }

    public void takeHappiness(Integer i) {
        Integer a = Optional.ofNullable((Integer) data.get("happiness")).orElse(0) - i;
        if (a < 0) {
            a = 0;
        }
        data.put("happiness", a);
        save();
    }

    public void setMood(mood m) {
        data.put("mood", m.toString());
        save();
    }

    public void setZombie(Boolean bol) {
        data.put("zombified", bol);
        save();
    }

    public Boolean getZombie() {
        return (Boolean) Optional.ofNullable(data.get("zombified")).orElse(false);
    }

    public mood getMood() {
        return mood.valueOf((String) Optional.ofNullable(data.get("mood")).orElse("NEUTRAL"));
    }

    public void setWork(Works i) {
        data.put("work", i.toString());
        save();
    }

    public Works getWork() {
        return Works.valueOf((String) Optional.ofNullable(data.get("work")).orElse("NONE"));
    }

    public void setLikes(Integer i, LivingEntity e) {
        data.put("likes-" + e.getUniqueId().toString(), i);
        save();
    }

    public void addLikes(Integer i, LivingEntity e) {
        data.put("likes-" + e.getUniqueId().toString(),
                Optional.ofNullable((Integer) data.get("likes-" + e.getUniqueId().toString())).orElse(0) + i);
        save();
    }

    public void takeLikes(Integer i, LivingEntity e) {
        Integer a = Optional.ofNullable((Integer) data.get("likes-" + e.getUniqueId().toString())).orElse(0) - i;
        if (a < 0) {
            a = 0;
        }
        data.put("likes-" + e.getUniqueId().toString(), a);

        villager.getLocation().getWorld().spawnParticle(org.bukkit.Particle.VILLAGER_ANGRY,
                villager.getLocation().clone().add(0, 1, 0), i + 5);
        save();
    }

    public String getRealName() {
        return ((String) data.get("name"))
                .replace(MComesToLife.getNames().getLang().getString("names.prefix", ""), "")
                .replace(MComesToLife.getNames().getLang().getString("names.suffix", ""), "");
    }

    public static String getRealName(String name) {
        return name
                .replace(MComesToLife.getNames().getLang().getString("names.prefix", ""), "")
                .replace(MComesToLife.getNames().getLang().getString("names.suffix", ""), "");
    }

    @Override
    public PlayerDisguise generateDisguise() {
        if (this.data.containsKey("skin") && this.data.containsKey("sex") && this.data.containsKey("name")) {
            SkinVariant vr = SkinVariant.SLIM;
            if (this.getSex() == sex.male) {
                vr = SkinVariant.CLASSIC;
            }
            PlayerDisguise disguise = new PlayerDisguise((String) this.data.get("name"));
            if (data.containsKey("skin-type")) {
                switch (data.get("skin-type").toString().toLowerCase()) {
                    case "file": {
                        disguise.setSkin(MComesToLife.getSkinHolder().getSkinFromPath(
                                ((String) this.data.get("skin")).replace("file:", "").replace("url:", "")
                                        .replace("name:", ""),
                                vr, disguise.getGameProfile(), this));
                        break;
                    }
                    case "url": {
                        try {
                            disguise.setSkin(MComesToLife.getSkinHolder().getSkinFromUrl(
                                    ((String) this.data.get("skin")).replace("file:", "").replace("url:", "")
                                            .replace("name:", ""),
                                    vr, disguise.getGameProfile()));
                        } catch (SkinRequestException e) {
                            disguise.setSkin(((String) this.data.get("skin")).replace("file:", "").replace("url:", "")
                                    .replace("name:", ""));
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "name": {
                        disguise.setSkin(((String) this.data.get("skin")).replace("file:", "").replace("url:", "")
                                .replace("name:", ""));
                        break;
                    }
                    case "data": {
                        LineConfig config = LineConfig.of(((String) this.data.get("skin")));
                        String value = config.getString("value", "");
                        String signature = config.getString("signature", "");
                        disguise.setSkin(MComesToLife.getSkinHolder().genSkinFromData(
                                value.replace("<unslash>", "/").replace("<equals>", "="),
                                signature.replace("<unslash>", "/").replace("<equals>", "="),
                                disguise.getGameProfile()));
                        break;
                    }
                    default: {
                        disguise.setSkin(((String) this.data.get("skin")).replace("file:", "").replace("url:", "")
                                .replace("name:", ""));
                        break;
                    }
                }
            } else {
                disguise.setSkin(
                        ((String) this.data.get("skin")).replace("file:", "").replace("url:", "").replace("name:", ""));
            }
            VillagerInventoryHolder inv = VillagerInventoryHolder.getInstance(this);
            inv.loadInventory();
            disguise = inv.loadDisguiseDisplay(disguise);
            messageUtils.Bukkitlog("Loaded disguise for:" + villager.getCustomName() + "{" + disguise.getSkin() + ","
                    + disguise.getWatcher().getItemInMainHand() + "}");
            return disguise;
        } else {
            messageUtils.Bukkitlog("Failed to load disguise for:" + villager.getCustomName());
            if (data.containsKey("sex"))
                messageUtils.Bukkitlog("sex: ✅");
            if (!data.containsKey("sex"))
                messageUtils.Bukkitlog("sex: <red>X");

            if (data.containsKey("name"))
                messageUtils.Bukkitlog("name: ✅");
            if (!data.containsKey("name"))
                messageUtils.Bukkitlog("name: <red>X");

            if (data.containsKey("skin"))
                messageUtils.Bukkitlog("skin: ✅");
            if (!data.containsKey("skin"))
                messageUtils.Bukkitlog("skin: <red>X");
            return new PlayerDisguise((String) this.data.get("name"));
        }
    }

    @Override
    public void Disguise() {
        DisguiseAPI.disguiseToAll(villager, generateDisguise());
    }

    @Override
    public void Disguise(Player... p) {
        DisguiseAPI.disguiseToPlayers(villager, generateDisguise(), p);
    }

    public void preDisguise() {
        String skin = getSkin((String) this.data.get("sex"), (String) this.data.get("type"));
        if (skin.startsWith("file:")) {
            skin = skin.replaceFirst("file:", "");
            tempData.put("skin-type", "file");
            data.put("skin-type", "file");
        } else if (skin.startsWith("url:")) {
            skin = skin.replaceFirst("url:", "");
            tempData.put("skin-type", "url");
            data.put("skin-type", "url");
        } else if (skin.startsWith("name:")) {
            skin = skin.replaceFirst("name:", "");
            tempData.put("skin-type", "name");
            data.put("skin-type", "name");
        } else if (skin.startsWith("data")) {
            tempData.put("skin-type", "data");
            data.put("skin-type", "data");
        } else {
            tempData.put("skin-type", "name");
            data.put("skin-type", "name");
        }
        data.put("skin", skin);
        save();
    }

    public String getSkin(String sex, String type) {
        ArrayList<String> sList = (ArrayList<String>) MComesToLife.getSkins().getLang()
                .getConfig().getStringList("skins." + sex.toLowerCase() + "." + type.toLowerCase());
        int index = DataMethods.rand(0, sList.size() - 1);
        return sList.get(index);
    }

    public String getName(String sex) {
        ArrayList<String> nList = (ArrayList<String>) MComesToLife.getNames().getLang()
                .getConfig().getStringList("names." + sex);
        int index = DataMethods.rand(0, nList.size() - 1);
        return MComesToLife.getNames().getLang().getString("names.prefix", "") + nList.get(index)
                + MComesToLife.getNames().getLang().getString("names.suffix", "");
    }

    public static String genName() {
        sex a = sex.male;
        if (DataMethods.rand(0, 1) == 1) {
            a = sex.female;
        }

        ArrayList<String> nList = (ArrayList<String>) MComesToLife.getNames().getLang()
                .getConfig().getStringList("names." + a);
        int index = DataMethods.rand(0, nList.size() - 1);
        return MComesToLife.getNames().getLang().getString("names.prefix", "") + nList.get(index)
                + MComesToLife.getNames().getLang().getString("names.suffix", "");
    }

    public void loadVillager(Boolean reApplySkin) {
        if (MComesToLife.getEnabledPlugins().isEnabled("MythicMobs")) {
            if (MythicBukkit.inst().getMobManager().isActiveMob(villager.getUniqueId())) {
                return;
            }
        }
        if (MComesToLife.getEnabledPlugins().isEnabled("Citizens")) {
            if (CitizensAPI.getNPCRegistry().isNPC(villager)) {
                return;
            }
        }

        if (MComesToLife.getEnabledPlugins().isEnabled("ShopKeepers")) {
            if (ShopkeepersAPI.getShopkeeperRegistry().isShopkeeper(villager)) {
                return;
            }
        }

        if (fileUtils.getFileConfiguration("data.yml").getConfig().contains(path())) {
            load();
            if (reApplySkin) {
                if (this.villager instanceof Villager) {
                    if (((Villager) this.villager).isAdult()) {
                        Disguise();
                    }
                } else {
                    Disguise();
                }
            }
            villager.setCustomName(getRealName());
        } else {
            setupVillager();
            this.villager.setPersistent(true);
            preDisguise();
            villager.setCustomName(getRealName());
            if (reApplySkin) {
                if (this.villager instanceof Villager) {
                    if (((Villager) this.villager).isAdult()) {
                        Disguise();
                    }
                } else {
                    Disguise();
                }
            }
            save();
        }

        if (MComesToLife.getPlugin().getConfig().getConfigurationSection("config.attributes") != null) {
            for (Attribute temp : Attribute.values()) {
                if (MComesToLife.config.getConfig()
                        .getString("config.attributes." + temp.toString().toLowerCase()) != null) {
                    this.setAttribute(temp, MComesToLife.config.getConfig()
                            .getDouble("config.attributes." + temp.toString().toLowerCase()));
                }
            }
        }
    }

    public sex getSex() {
        if (data.get("sex") != "any") {
            if (data.get("sex").toString().equalsIgnoreCase("male")) {
                return sex.male;
            }
            if (data.get("sex").toString().equalsIgnoreCase("female")) {
                return sex.female;
            }
        }
        return sex.male;
    }

    public void divorce(LivingEntity a) {
        if (DataMethods.getRelationMap(a).get("spouse") != "any") {
            if (MComesToLife.getPlugin().getServer().getOfflinePlayer(
                    UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse"))) != null) {

                if (MComesToLife.getPlugin().getServer().getOnlinePlayers()
                        .contains(MComesToLife.getPlugin().getServer().getOfflinePlayer(
                                UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse"))))) {
                    Player spouse = (Player) MComesToLife.getPlugin().getServer().getOfflinePlayer(
                            UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse")));
                    messageUtils.MessageParsedPlaceholders((CommandSender) spouse, "cmd.divorce.reciver",
                            "<prefix><gray><spouse_name> se a divorciado de ti</gray>");
                    messageUtils.MessageParsedPlaceholders((CommandSender) a, "cmd.divorce.sender",
                            "<prefix><gray>Te has divorciado de <spouse_name></gray>");
                    DataMethods.setData("spouse", null, a);
                    DataMethods.setData("spouse", null, (LivingEntity) spouse);
                    if (MComesToLife.config.getBoolean("config.divorce.broadcast.all", false)) {
                        Message msg = new Message(MComesToLife.getMessages().getLang("cmd.divorce.broadcast",
                                "<prefix><gray><spouse_one> se a divorciado de <spouse_two></gray>"));
                        msg.replace("<spouse_one>", a.getName());
                        msg.replace("<spouse_two>", spouse.getName());
                        if (MComesToLife.config.getBoolean("config.divorce.broadcast.ignore_spouses", false)) {
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (p.getUniqueId() != a.getUniqueId() && p.getUniqueId() != spouse.getUniqueId()) {
                                    messageUtils.MessageParsedPlaceholders(p, msg);
                                }
                            }
                        } else {
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                messageUtils.MessageParsedPlaceholders(p, msg);
                            }
                        }
                    }
                }
            } else {

                if (Bukkit.getServer()
                        .getEntity(UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse"))) != null) {
                    messageUtils.MessageParsedPlaceholders((CommandSender) a, "cmd.divorce.sender",
                            "<prefix><gray>Te has divorciado de <spouse_name></gray>");
                    DataMethods.setData("spouse", null, a);
                    DataMethods.setData("spouse", null, (LivingEntity) Bukkit.getServer()
                            .getEntity(UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse"))));
                    Message msg = new Message(MComesToLife.getMessages().getLang("cmd.divorce.broadcast",
                            "<prefix><gray><spouse_one> se a divorciado de <spouse_two></gray>"));
                    msg.replace("<spouse_one>", a.getName());
                    msg.replace("<spouse_two>",
                            Bukkit.getServer()
                                    .getEntity(UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse")))
                                    .getName());
                    if (MComesToLife.config.getBoolean("config.divorce.broadcast.ignore_spouses", false)) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            if (p.getUniqueId() != a.getUniqueId() && p.getUniqueId() != Bukkit.getServer()
                                    .getEntity(UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse")))
                                    .getUniqueId()) {
                                messageUtils.MessageParsedPlaceholders(p, msg);
                            }
                        }
                    } else {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            messageUtils.MessageParsedPlaceholders(p, msg);
                        }
                    }
                }
            }
        }
    };

    public void divorceWithOutMessages(LivingEntity a) {

        if (DataMethods.getRelationMap(a).get("spouse") != "any") {
            if (MComesToLife.getPlugin().getServer().getOfflinePlayer(
                    UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse"))) != null) {

                if (MComesToLife.getPlugin().getServer().getOnlinePlayers()
                        .contains(MComesToLife.getPlugin().getServer().getOfflinePlayer(
                                UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse"))))) {
                    Player spouse = (Player) MComesToLife.getPlugin().getServer().getOfflinePlayer(
                            UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse")));
                    DataMethods.setData("spouse", null, a);
                    DataMethods.setData("spouse", null, (LivingEntity) spouse);
                }
            } else {

                if (Bukkit.getServer()
                        .getEntity(UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse"))) != null) {
                    DataMethods.setData("spouse", null, a);
                    DataMethods.setData("spouse", null, (LivingEntity) Bukkit.getServer()
                            .getEntity(UUID.fromString((String) DataMethods.getRelationMap(a).get("spouse"))));
                }
            }
        }
    };

    public void setupVillager() {
        if (fileUtils.getBoolean("config.yml", "config.autoChangeVillagers")) {
            try {
                String sex = "female";
                if ((new Random()).nextInt(2) + 1 == 2)
                    sex = "male";
                String type = "none";
                if (villager instanceof Villager) {
                    Villager vil = (Villager) villager;
                    if (vil.getProfession().equals(Villager.Profession.NONE)) {
                        Villager.Profession[] split = Villager.Profession.values();
                        ArrayList<Villager.Profession> list = new ArrayList<>();
                        for (int i = 0; i < split.length; i++) {
                            if (!split[i].equals(Villager.Profession.NONE))
                                list.add(split[i]);
                        }
                        Profession p = list.get(DataMethods.rand(1, list.size()) - 1);
                        vil.setProfession(p);
                        ((Villager) villager).setProfession(p);
                        ((Villager) this.villager).setVillagerLevel(2);
                        ((Villager) this.villager).setVillagerExperience(10);
                        ((Villager) villager).setProfession(p);
                    }
                    type = vil.getProfession().toString().toLowerCase();
                } else {
                    type = villager.getType().toString().toLowerCase();
                }
                trait t = trait.values()[DataMethods.rand(1, trait.values().length - 1)];
                mood m = mood.values()[DataMethods.rand(1, mood.values().length - 1)];
                String skin = getSkin(sex, type);
                String name = getName(sex);
                data.put("mood", m.toString());
                data.put("sex", sex);
                data.put("trait", t.toString());
                data.put("skin", skin);
                data.put("name", name);
                data.put("type", type);
                data.put("tittle", MComesToLife.getProffesions().getLang("prefix", "")
                        + MComesToLife.getProffesions().getLang(type, type.toLowerCase().replace("_", " ")));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void killVillager() {
        String spouseuuid = (String) DataMethods.getRelationMap(this.villager).get("spouse");
        if (spouseuuid != "any") {
            // get the player by the UUID
            Optional<Player> opt = Optional.empty();
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.getUniqueId().toString().equalsIgnoreCase(spouseuuid)) {
                    opt = Optional.ofNullable(p);
                }
            }
            opt.ifPresent(player -> {
                Message death;
                if (DataMethods.getSex(player) == sex.male) {
                    death = new Message(MComesToLife.messages.getLang("cmd.villager.death-female",
                            "<prefix><gray>Tu esposa <spouse_name> a muerto"));
                } else {
                    death = new Message(MComesToLife.messages.getLang("cmd.villager.death-female",
                            "<prefix><gray>Tu esposo <spouse_name> a muerto"));
                }
                this.divorceWithOutMessages(player);
            });
        }

        removeData();
    }

}
