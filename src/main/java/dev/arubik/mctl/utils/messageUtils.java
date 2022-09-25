package dev.arubik.mctl.utils;

import java.util.HashMap;
import java.util.Optional;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.lib.adventure.audience.Audience;
import io.lumine.mythic.lib.adventure.audience.MessageType;
import io.lumine.mythic.lib.adventure.platform.bukkit.BukkitAudiences;
import io.lumine.mythic.lib.adventure.text.Component;
import io.lumine.mythic.lib.adventure.text.minimessage.MiniMessage;
import io.lumine.mythic.lib.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import io.lumine.mythic.lib.adventure.text.serializer.legacy.LegacyComponentSerializer;
import io.lumine.mythic.lib.adventure.title.TitlePart;
import lombok.Getter;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.enums.sex;
import dev.arubik.mctl.holders.Methods.DataMethods;

public class messageUtils {
    public static final BukkitAudiences au = BukkitAudiences.create(MComesToLife.getPlugin());

    public static void Message(Player pl, String s) {
        Audience p = Audience.empty();
        p = au.player(pl);

        Component parsed = mm.deserialize(s.toString());
        messageUtils.sendMessage(p, parsed, 0);

    }

    private static ConsoleCommandSender console = MComesToLife.getPlugin().getServer().getConsoleSender();

    public static void log(final String message) {
        messageUtils.Message(console, message);
    }

    public static void Bukkitlog(final String message) {
        if (fileUtils.getBoolean("config.yml", "config.debug", true)) {
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    public static void Message(CommandSender pl, String s) {

        Audience p = Audience.empty();
        p = au.sender(pl);

        Component parsed = mm.deserialize(s.toString());
        messageUtils.sendMessage(p, parsed, 0);
    }

    private static MiniMessage mm = MiniMessage.miniMessage();

    public static void MiniMessage(Object s, Player player, int id /* 0: message 1: actionbar */) {
        Component parsed = mm.deserialize(s.toString());
        BungeeComponentSerializer comp = BungeeComponentSerializer.get();
        player.spigot().sendMessage(comp.serialize(parsed));
    }

    // ConsoleCommandSender
    public static void MiniMessage(Object s, @Nullable ConsoleCommandSender player,
            int id /* 0: message 1: actionbar */) {
        if (player == null) {

            Audience p = Audience.empty();
            p = au.sender(Bukkit.getConsoleSender());

            Component parsed = mm.deserialize(s.toString());
            messageUtils.sendMessage(p, parsed, id);
        } else {

            Audience p = Audience.empty();
            p = au.sender(player);

            Component parsed = mm.deserialize(s.toString());
            messageUtils.sendMessage(p, parsed, id);
        }
    }

    // CommandSender
    public static void MiniMessage(Object s, CommandSender player, int id /* 0: message 1: actionbar */) {
        if (player instanceof Player) {
            messageUtils.MiniMessage(s, (Player) player, id);
            return;
        }
        Audience p = Audience.empty();

        p = au.sender(player);

        Component parsed = mm.deserialize(s.toString());
        messageUtils.sendMessage(p, parsed, id);
    }

    public static void sendMessage(Audience p, Component parsed, int id) {

        if (id == 0) {
            p.sendMessage(parsed);
        } else if (id == 1) {
            p.sendActionBar(parsed);
        } else if (id == 2) {
            p.sendTitlePart(TitlePart.TITLE, parsed);
        } else if (id == 3) {
            p.sendTitlePart(TitlePart.SUBTITLE, parsed);
        } else if (id == 5) {
            p.sendMessage(parsed, MessageType.SYSTEM);
        } else {
            p.sendMessage(parsed);
        }
    }

    public static void MessageParsedPlaceholders(CommandSender p, String path, String value) {
        dev.arubik.mctl.holders.Message message = new dev.arubik.mctl.holders.Message(
                MComesToLife.getMessages().getLang(path, value));

        message.replace("<prefix>", MComesToLife.getMessages().getLang("prefix", "<red>[MCTL]</red> "));
        message.replace("<random_name>", CustomVillager.genName());
        message.replace(new String[] { "<cmd>", "<command>" }, MComesToLife.getMessages().getLang("command", "mctl"));
        if (p instanceof Player) {
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            if (message.getString().contains("<player_sex")) {
                message.formatPlayerSex(DataMethods.getSex((Player) p));
            }
            if (message.getString().contains("<contrary_sex")) {
                message.ContraryformatSex(DataMethods.getSex((Player) p));
            }
            message.setPlayer((Player) p);
            message.replace("%player%", p.getName());

            String nice = MComesToLife.getMessages().getLang("message.gender.female.nice", "beautiful");
            String type = MComesToLife.getMessages().getLang("message.gender.female.type", "girl");

            if (sex.valueOf(Optional.ofNullable(DataMethods.retrivePlayerData((Player) p).get("sex")).orElse("male")
                    .toString()) == sex.male) {
                nice = MComesToLife.getMessages().getLang("message.gender.male.nice", "handsome");
                type = MComesToLife.getMessages().getLang("message.gender.male.type", "boy");
            }
            message.replace("<player_nice>", nice);
            message.replace("<player_type>", type);
            LivingEntity liv = (LivingEntity) ((Player) p);
            message.replace("<spouse_name>", DataMethods.getSpouse(liv).orElse(liv).getCustomName());
        }
        messageUtils.MiniMessage(message.getString(), p, 0);
    }

    public static void MessageParsedPlaceholders(CommandSender p, dev.arubik.mctl.holders.Message message) {
        message.replace("<prefix>", MComesToLife.getMessages().getLang("prefix", "<red>[MCTL]</red> "));
        message.replace("<random_name>", CustomVillager.genName());
        message.replace(new String[] { "<cmd>", "<command>" }, MComesToLife.getMessages().getLang("command", "mctl"));
        if (p instanceof Player) {
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            if (message.getString().contains("<player_sex")) {
                message.formatPlayerSex(DataMethods.getSex((Player) p));
            }
            if (message.getString().contains("<contrary_sex")) {
                message.ContraryformatSex(DataMethods.getSex((Player) p));
            }
            message.setPlayer((Player) p);
            message.replace("%player%", p.getName());
            String nice = MComesToLife.getMessages().getLang("message.gender.female.nice", "beautiful");
            String type = MComesToLife.getMessages().getLang("message.gender.female.type", "girl");

            if (sex.valueOf(Optional.ofNullable(DataMethods.retrivePlayerData((Player) p).get("sex")).orElse("male")
                    .toString()) == sex.male) {
                nice = MComesToLife.getMessages().getLang("message.gender.male.nice", "handsome");
                type = MComesToLife.getMessages().getLang("message.gender.male.type", "boy");
            }
            message.replace("<player_nice>", nice);
            message.replace("<player_type>", type);
            LivingEntity liv = (LivingEntity) ((Player) p);
            message.replace("<spouse_name>", DataMethods.getSpouse(liv).orElse(liv).getCustomName());
        }
        messageUtils.MiniMessage(message.getString(), p, 0);
    }

    public static String StringParsedPlaceholders(CommandSender p, dev.arubik.mctl.holders.Message message) {
        message.replace("<prefix>", MComesToLife.getMessages().getLang("prefix", "<red>[MCTL]</red> "));
        message.replace("<random_name>", CustomVillager.genName());
        message.replace(new String[] { "<cmd>", "<command>" }, MComesToLife.getMessages().getLang("command", "mctl"));
        if (p instanceof Player) {
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            if (message.getString().contains("<player_sex")) {
                message.formatPlayerSex(DataMethods.getSex((Player) p));
            }
            if (message.getString().contains("<contrary_sex")) {
                message.ContraryformatSex(DataMethods.getSex((Player) p));
            }
            message.setPlayer((Player) p);
            message.replace("%player%", p.getName());
            String nice = MComesToLife.getMessages().getLang("message.gender.female.nice", "beautiful");
            String type = MComesToLife.getMessages().getLang("message.gender.female.type", "girl");

            if (sex.valueOf(Optional.ofNullable(DataMethods.retrivePlayerData((Player) p).get("sex")).orElse("male")
                    .toString()) == sex.male) {
                nice = MComesToLife.getMessages().getLang("message.gender.male.nice", "handsome");
                type = MComesToLife.getMessages().getLang("message.gender.male.type", "boy");
            }
            message.replace("<player_nice>", nice);
            message.replace("<player_type>", type);
            LivingEntity liv = (LivingEntity) ((Player) p);
            message.replace("<spouse_name>", DataMethods.getSpouse(liv).orElse(liv).getCustomName());
        }
        return message.getString();
    }

    public static void MessageParsedPlaceholders(CommandSender p, dev.arubik.mctl.holders.Message message,
            Player second) {
        message.replace("<prefix>", MComesToLife.getMessages().getLang("prefix", "<red>[MCTL]</red> "));
        message.replace("<random_name>", CustomVillager.genName());
        message.replace(new String[] { "<cmd>", "<command>" }, MComesToLife.getMessages().getLang("command", "mctl"));
        if (p instanceof Player) {
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            if (message.getString().contains("<player_sex")) {
                message.formatPlayerSex(DataMethods.getSex((Player) p));
            }
            if (message.getString().contains("<contrary_sex")) {
                message.ContraryformatSex(DataMethods.getSex((Player) p));
            }
            message.setPlayer((Player) p);
            message.replace("%player%", p.getName());

            String nice = MComesToLife.getMessages().getLang("message.gender.female.nice", "beautiful");
            String type = MComesToLife.getMessages().getLang("message.gender.female.type", "girl");

            if (sex.valueOf(Optional.ofNullable(DataMethods.retrivePlayerData((Player) p).get("sex")).orElse("male")
                    .toString()) == sex.male) {
                nice = MComesToLife.getMessages().getLang("message.gender.male.nice", "handsome");
                type = MComesToLife.getMessages().getLang("message.gender.male.type", "boy");
            }
            message.replace("<player_nice>", nice);
            message.replace("<player_type>", type);
            LivingEntity liv = (LivingEntity) ((Player) p);
            message.replace("<spouse_name>", DataMethods.getSpouse(liv).orElse(liv).getCustomName());
        }
        message.replace("<second_name>", second.getDisplayName());
        message.replace("<second_displayname>", second.getDisplayName());
        messageUtils.MiniMessage(message.getString(), p, 0);
    }

    public static void MessageParsedPlaceholders(CommandSender p, dev.arubik.mctl.holders.Message message,
            CustomVillager v) {

        message.replace("<prefix>", MComesToLife.getMessages().getLang("prefix", "<red>[MCTL]</red> "));
        message.replace("<random_name>", CustomVillager.genName());
        message.replace(new String[] { "<cmd>", "<command>" }, MComesToLife.getMessages().getLang("command", "mctl"));

        if (p instanceof Player) {
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            if (message.getString().contains("<player_sex")) {
                message.formatPlayerSex(DataMethods.getSex((Player) p));
            }
            if (message.getString().contains("<contrary_sex")) {
                message.ContraryformatSex(DataMethods.getSex((Player) p));
            }
            message.setPlayer((Player) p);
            message.replace("%player%", p.getName());
            final Player pl = (Player) p;
            String nice = MComesToLife.getMessages().getLang("message.gender.female.nice", "beautiful");
            String type = MComesToLife.getMessages().getLang("message.gender.female.type", "girl");

            if (sex.valueOf(Optional.ofNullable(DataMethods.retrivePlayerData((Player) p).get("sex")).orElse("male")
                    .toString()) == sex.male) {
                nice = MComesToLife.getMessages().getLang("message.gender.male.nice", "handsome");
                type = MComesToLife.getMessages().getLang("message.gender.male.type", "boy");
            }
            message.replace("<player_nice>", nice);
            message.replace("<player_type>", type);
            message.replace("<villager_nice>", MComesToLife.getMessages()
                    .getLang("message.gender." + v.getSex().toString().toLowerCase() + ".nice", "beautiful"));
            message.replace("<villager_name>", v.getRealName());
            message.replace("<villager_prefix>", MComesToLife.getNames().getLang().getString("names.prefix", ""));
            message.replace("<villager_suffix>", MComesToLife.getNames().getLang().getString("names.suffix", ""));
            message.replace("<villager_displayname>", ((String) v.getData().get("name")));
            message.replace("<villager_sons>", DataMethods.getSonNames(v.villager));
            message.replace("<villager_type>", MComesToLife.getMessages()
                    .getLang("message.gender." + v.getSex().toString().toLowerCase() + ".nice", "girl"));
            if (message.getString().contains("<villager_likes>")) {
                message.replace("<villager_likes>", v.getLikes((Player) p).orElse(0).toString());
            }
            HashMap<String, Object> data = DataMethods.retriveData(v.villager);
            if (data.get("father") != null && data.get("mother") != null) {
                if (data.get("father").toString().equalsIgnoreCase(pl.getUniqueId().toString())) {
                    message.replace("<villager_parent_me>",
                            MComesToLife.getMessages().getLang("message.gender.female.parent", "mother"));
                    message.replace("<villager_parent_other>",
                            MComesToLife.getMessages().getLang("message.gender.male.parent", "father"));
                }
                if (data.get("mother").toString().equalsIgnoreCase(pl.getUniqueId().toString())) {
                    message.replace("<villager_parent_other>",
                            MComesToLife.getMessages().getLang("message.gender.female.parent", "mother"));
                    message.replace("<villager_parent_me>",
                            MComesToLife.getMessages().getLang("message.gender.male.parent", "father"));
                }
            }
            LivingEntity liv = (LivingEntity) ((Player) p);
            message.replace("<spouse_name>", DataMethods.getSpouse(liv).orElse(liv).getCustomName());
        }
        if (message.getString().contains("§")) {
            message.removeLegacy();
        }
        if (message.getString().contains("<villager_sex")) {
            message.formatSex(v.getSex());
        }
        messageUtils.MiniMessage(message.getString(), p, 0);
    }

    public static String StringParsedPlaceholders(Player p, dev.arubik.mctl.holders.Message message,
            CustomVillager v) {

        message.replace("<prefix>", MComesToLife.getMessages().getLang("prefix", "<red>[MCTL]</red> "));
        message.replace("<random_name>", CustomVillager.genName());
        message.replace(new String[] { "<cmd>", "<command>" }, MComesToLife.getMessages().getLang("command", "mctl"));

        if (p instanceof Player) {
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            message.replace("<player_sons>", DataMethods.getSonNames((Player) p));
            if (message.getString().contains("<player_sex")) {
                message.formatPlayerSex(DataMethods.getSex((Player) p));
            }
            if (message.getString().contains("<contrary_sex")) {
                message.ContraryformatSex(DataMethods.getSex((Player) p));
            }
            message.setPlayer((Player) p);
            message.replace("%player%", p.getName());
            final Player pl = (Player) p;
            String nice = MComesToLife.getMessages().getLang("message.gender.female.nice", "beautiful");
            String type = MComesToLife.getMessages().getLang("message.gender.female.type", "girl");

            if (sex.valueOf(Optional.ofNullable(DataMethods.retrivePlayerData((Player) p).get("sex")).orElse("male")
                    .toString()) == sex.male) {
                nice = MComesToLife.getMessages().getLang("message.gender.male.nice", "handsome");
                type = MComesToLife.getMessages().getLang("message.gender.male.type", "boy");
            }
            message.replace("<player_nice>", nice);
            message.replace("<player_type>", type);
            message.replace("<villager_nice>", MComesToLife.getMessages()
                    .getLang("message.gender." + v.getSex().toString().toLowerCase() + ".nice", "beautiful"));
            message.replace("<villager_name>", v.getRealName());
            message.replace("<villager_prefix>", MComesToLife.getNames().getLang().getString("names.prefix", ""));
            message.replace("<villager_suffix>", MComesToLife.getNames().getLang().getString("names.suffix", ""));
            message.replace("<villager_displayname>", ((String) v.getData().get("name")));

            message.replace("<villager_sons>", DataMethods.getSonNames(v.villager));
            message.replace("<villager_type>", MComesToLife.getMessages()
                    .getLang("message.gender." + v.getSex().toString().toLowerCase() + ".nice", "girl"));
            if (message.getString().contains("<villager_likes>")) {
                message.replace("<villager_likes>", v.getLikes(p).orElse(0).toString());
            }

            HashMap<String, Object> data = DataMethods.retriveData(v.villager);
            if (data.get("father") != null && data.get("mother") != null) {
                if (data.get("father").toString().equalsIgnoreCase(pl.getUniqueId().toString())) {
                    message.replace("<villager_parent_me>",
                            MComesToLife.getMessages().getLang("message.gender.female.parent", "mother"));
                    message.replace("<villager_parent_other>",
                            MComesToLife.getMessages().getLang("message.gender.male.parent", "father"));
                }
                if (data.get("mother").toString().equalsIgnoreCase(pl.getUniqueId().toString())) {
                    message.replace("<villager_parent_other>",
                            MComesToLife.getMessages().getLang("message.gender.female.parent", "mother"));
                    message.replace("<villager_parent_me>",
                            MComesToLife.getMessages().getLang("message.gender.male.parent", "father"));
                }
            }
            LivingEntity liv = (LivingEntity) ((Player) p);
            message.replace("<spouse_name>", DataMethods.getSpouse(liv).orElse(liv).getCustomName());
        }
        if (message.getString().contains("§")) {
            message.removeLegacy();
        }
        if (message.getString().contains("<villager_sex")) {
            message.formatSex(v.getSex());
        }
        return message.getString();
    }

    private static LegacyComponentSerializer leg = LegacyComponentSerializer.builder().build();

}
