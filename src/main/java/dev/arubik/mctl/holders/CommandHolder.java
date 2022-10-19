package dev.arubik.mctl.holders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.jetbrains.annotations.NotNull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.entity.WorldEntityLoader;
import dev.arubik.mctl.enums.TypeAction;
import dev.arubik.mctl.enums.Sex;
import dev.arubik.mctl.events.listeners.EntityListener;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.Target;
import dev.arubik.mctl.utils.TimeUtils;
import dev.arubik.mctl.utils.FileUtils;
import dev.arubik.mctl.utils.MessageUtils;

public class CommandHolder implements org.bukkit.command.TabExecutor {

    // mesage example

    // MessageUtils.MessageParsedPlaceholders(sender,
    // new Message(
    // MComesToLife.getMessages().getLang("cmd.help",
    // "<prefix><gray>El comando no existe has /<command> help para ver los
    // comandos</gray>")));
    //

    private Boolean admin(CommandSender sender) {
        return (sender.hasPermission("mctl.admin") || sender.isOp());
    }

    private Boolean default_commands = FileUtils.getFileConfiguration("config.yml")
            .getBoolean("config.permission.default-commands", true);

    public enum CommandArg0 {
        gender,
        reload,
        fixVillager,
        marry,
        trade,
        loadAllVillagers,
        debug,
        version,
        help;

        public static Boolean contains(String arg) {
            for (CommandArg0 arg0 : CommandArg0.values()) {
                if (arg0.toString().equalsIgnoreCase(arg)) {
                    return true;
                }
            }
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {
        if (!sender.hasPermission("mctl.command"))
            return null;

        if (args.length == 1) {
            List<String> arrayList = new ArrayList<>();
            if (sender.hasPermission("mctl.gender.command") || admin(sender) || default_commands) {
                arrayList.add("gender");
            }
            if (sender.hasPermission("mctl.gender.marry") || admin(sender) || default_commands) {
                arrayList.add("marry");
            }
            if (sender.hasPermission("mctl.help") || admin(sender) || default_commands) {
                arrayList.add("help");
            }
            if (sender.hasPermission("mctl.admin.reload") || admin(sender)) {
                arrayList.add("reload");
            }
            if (sender.hasPermission("mctl.villager.fix") || admin(sender)) {
                arrayList.add("fixVillager");
            }
            if (sender.hasPermission("mctl.villager.trade") || admin(sender)) {
                arrayList.add("trade");
            }
            if (sender.hasPermission("mctl.admin.reload") || admin(sender)) {
                arrayList.add("loadAllVillagers");
            }
            if (sender.hasPermission("mctl.cmd.debug") || admin(sender)) {
                arrayList.add("debug");
            }
            // for (String cmd : arrayList) {
            // if (cmd.startsWith(args[0]))
            // arrayList.remove(cmd);
            // arrayList.add(cmd);
            // }
            return arrayList;
        }
        if (args.length == 2) {
            try {
                CommandArg0.valueOf(args[0]);
            } catch (IllegalArgumentException e) {
                return null;
            }
            if (CommandArg0.valueOf(args[0]) != null) {
                switch (CommandArg0.valueOf(args[0])) {
                    case debug: {

                        List<String> arrayList = new ArrayList<>();
                        for (TypeAction a : TypeAction.values()) {
                            arrayList.add(a.toString());
                        }
                        return arrayList;
                    }
                    case fixVillager:
                        break;
                    case marry: {
                        // add all players to the list and "accept" and "deny"
                        List<String> arrayList = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            arrayList.add(player.getName());
                        }
                        arrayList.add("accept");
                        arrayList.add("deny");
                        arrayList.add("procreate");
                        return arrayList;
                    }
                    case gender: {
                        List<String> arrayList = new ArrayList<>();
                        if (sender.hasPermission("mctl.gender.male") || admin(sender) || default_commands) {
                            arrayList.add("male");
                        }
                        if (sender.hasPermission("mctl.gender.female") || admin(sender) || default_commands) {
                            arrayList.add("female");
                        }
                        return arrayList;
                    }
                    case reload:
                        break;
                    case help:
                        break;
                    default:
                        break;

                }
            }
        }

        return null;
    }

    public HashMap<OfflinePlayer, OfflinePlayer> marryRequest = new HashMap<OfflinePlayer, OfflinePlayer>();

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (!sender.hasPermission("mctl.command")) {
            MessageUtils.MessageParsedPlaceholders(sender,
                    new Message(
                            MComesToLife.getMessages().getLang("cmd.no-permission",
                                    "<prefix><gray>No tienes permisos para ejecutar este comando</gray>")));
            return true;
        }
        int size = args.length;
        if (size >= 1) {
            if (!CommandArg0.contains(args[0])) {
                MessageUtils.MessageParsedPlaceholders(sender,
                        new Message(
                                MComesToLife.getMessages().getLang("cmd.help",
                                        "<prefix><gray>Los argumentos son muy cortos has /<command> help para ver los comandos</gray>")));
                return true;
            }

            if (CommandArg0.valueOf(args[0]) != null) {
                switch (CommandArg0.valueOf(args[0])) {
                    case version: {
                        if (sender.hasPermission("mctl.version")) {
                            MessageUtils.MessageParsedPlaceholders(sender,
                                    new Message("<prefix><gray>La version de MComesToLife es <version></gray>"));
                            MessageUtils.MessageParsedPlaceholders(sender,
                                    new Message("<prefix><gray>La version del servidor es <server_version></gray>"));
                        }
                    }
                    case gender: {
                        if (sender.hasPermission("mctl.gender") || sender.hasPermission("mctl.gender.male")
                                || sender.hasPermission("mctl.gender.female") || admin(sender)) {

                            if (size >= 2) {
                                if (!Sex.contains(args[1])) {
                                    MessageUtils.MessageParsedPlaceholders(sender,
                                            new Message(
                                                    MComesToLife.getMessages().getLang("cmd.gender.no-args",
                                                            "<prefix><gray>La forma de uso correcta es /<command> gender male/female</gray>")));
                                    return true;
                                }

                                if (!DataMethods.getRelationMap((Player) sender).get("spouse").toString()
                                        .equalsIgnoreCase("any")) {
                                    MessageUtils.MessageParsedPlaceholders(sender,
                                            new Message(
                                                    MComesToLife.getMessages().getLang("cmd.gender.married",
                                                            "<prefix><gray>No puedes cambiar tu sexo mientras estas casado</gray>")));
                                }

                                Message message = new Message(
                                        MComesToLife.getMessages().getLang("cmd.gender.success",
                                                "<prefix><gray>Felicidades has cambiado con exito tu sexo a <sex></gray>")
                                                .replace("<sex>",
                                                        MComesToLife.getMessages().getLang(
                                                                "cmd.gender."
                                                                        + Sex.valueOf(args[1]).toString().toLowerCase(),
                                                                Sex.valueOf(args[1]).toString().toLowerCase()))
                                                .replace("<sex_lowercase>",
                                                        MComesToLife.getMessages().getLang(
                                                                "cmd.gender."
                                                                        + Sex.valueOf(args[1]).toString().toLowerCase(),
                                                                Sex.valueOf(args[1]).toString().toLowerCase())
                                                                .toLowerCase()));
                                DataMethods.setSex((LivingEntity) ((Player) sender), Sex.valueOf(args[1]));
                                MessageUtils.MessageParsedPlaceholders(sender, message);
                                return true;
                            } else {
                                MessageUtils.MessageParsedPlaceholders(sender,
                                        new Message(
                                                MComesToLife.getMessages().getLang("cmd.gender.no-args",
                                                        "<prefix><gray>La forma de uso correcta es /<command> gender male/female</gray>")));
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                    case marry: {

                        if (args[1].equalsIgnoreCase("procreate")) {
                            if (sender instanceof Player) {
                                if (DataMethods.getRelationMap((Player) sender).get("spouse").toString()
                                        .equalsIgnoreCase("any")) {
                                    MessageUtils.MessageParsedPlaceholders(sender,
                                            new Message(
                                                    MComesToLife.getMessages().getLang("cmd.marry.procreate.no-spouse",
                                                            "<prefix><gray>No tienes pareja para procrear</gray>")));
                                    return true;
                                } else {
                                    // verify if the spouse is online
                                    String uuid = DataMethods.getRelationMap((Player) sender).get("spouse")
                                            .toString();
                                    if (Timers.getPlayerByUuid(uuid) != null) {
                                        Player p2 = Timers.getPlayerByUuid(uuid);
                                        Player p1 = (Player) sender;
                                        Long p1l = (Long) DataMethods.retrivePlayerData(p1).getOrDefault("last-baby",
                                                0L);
                                        Long p2l = (Long) DataMethods.retrivePlayerData(p2).getOrDefault("last-baby",
                                                0L);
                                        if (p1l <= 0L && p2l <= 0L) {
                                            if (p1.getLocation().distance(p2.getLocation()) <= 4) {
                                                // play hearth particles via Bukkit
                                                p1.getWorld().spawnParticle(Particle.HEART, p1.getLocation(), 15);
                                                p2.getWorld().spawnParticle(Particle.HEART, p2.getLocation(), 15);

                                                // set the current time in millis to the player data
                                                DataMethods.setData("last-baby", System.currentTimeMillis(), p1);
                                                DataMethods.setData("last-baby", System.currentTimeMillis(), p2);

                                                // spawn baby

                                                if (DataMethods.getSex(p1) == Sex.male) {
                                                    DataMethods.loadBabyVillager(true, p1, p2);
                                                } else {
                                                    DataMethods.loadBabyVillager(true, p2, p1);
                                                }

                                                // tell message your baby grow up
                                                MessageUtils.MessageParsedPlaceholders(p1,
                                                        new Message(
                                                                MComesToLife.getMessages().getLang(
                                                                        "cmd.marry.procreate.success",
                                                                        "<prefix><gray>Tu pareja ha tenido un bebe con tigo</gray>")));
                                            } else {
                                                // your spouse is too far
                                                MessageUtils.MessageParsedPlaceholders(p1,
                                                        new Message(
                                                                MComesToLife.getMessages().getLang(
                                                                        "cmd.marry.procreate.too-far",
                                                                        "<prefix><gray>Tu pareja esta muy lejos para procrear</gray>")));
                                            }
                                        } else if (p2l >= p2l + TimeUtils.getTimeOutProcreate()) {
                                            MessageUtils.MessageParsedPlaceholders(sender,
                                                    new Message(
                                                            MComesToLife.getMessages().getLang(
                                                                    "cmd.marry.procreate.timeout",
                                                                    "<prefix><gray>Tu pareja ya ha tenido un hijo recientemente</gray>")));

                                            Message timetoWait = new Message(
                                                    MComesToLife.getMessages().getLang("cmd.timeout",
                                                            "<prefix><gray>Debes Esperar <day_amount> <day_format> <hours_amount> <hours_format> <minutes_amount> <minutes_format> <seconds_amount> <seconds_format>/gray>"));
                                            Long currentTime = p2l + TimeUtils.getTimeOutProcreate()
                                                    - System.currentTimeMillis();
                                            timetoWait.setupTimePlaceholder(currentTime);
                                            MessageUtils.MessageParsedPlaceholders(sender, timetoWait);
                                            return true;
                                        } else if (p1l >= p2l + TimeUtils.getTimeOutProcreate()) {
                                            MessageUtils.MessageParsedPlaceholders(sender,
                                                    new Message(
                                                            MComesToLife.getMessages().getLang(
                                                                    "cmd.marry.procreate.timeout",
                                                                    "<prefix><gray>Tu yas ha tenido un hijo recientemente</gray>")));

                                            Message timetoWait = new Message(
                                                    MComesToLife.getMessages().getLang("cmd.timeout",
                                                            "<prefix><gray>Debes Esperar <day_amount> <day_format> <hours_amount> <hours_format> <minutes_amount> <minutes_format> <seconds_amount> <seconds_format>/gray>"));
                                            Long currentTime = p2l + TimeUtils.getTimeOutProcreate()
                                                    - System.currentTimeMillis();
                                            timetoWait.setupTimePlaceholder(currentTime);
                                            MessageUtils.MessageParsedPlaceholders(sender, timetoWait);
                                            return true;
                                        }
                                    } else {
                                        Player p1 = (Player) sender;
                                        LivingEntity p2 = p1;
                                        Boolean a = false;
                                        for (Entity e : p1.getWorld().getEntities()) {
                                            if (e.getUniqueId().toString().equalsIgnoreCase(uuid)
                                                    && !(e instanceof Player)) {
                                                p2 = (LivingEntity) e;
                                                a = true;
                                                break;
                                            }
                                        }

                                        if (!a) {
                                            MessageUtils.MessageParsedPlaceholders(p1,
                                                    new Message(
                                                            MComesToLife.getMessages().getLang(
                                                                    "cmd.marry.procreate.offline",
                                                                    "<prefix><gray>Tu pareja esta desconectada</gray>")));
                                        } else {
                                            Long p1l = (Long) DataMethods.retrivePlayerData(p1)
                                                    .getOrDefault("last-baby", 0L);
                                            Long p2l = (Long) DataMethods.retriveData(p2).getOrDefault("last-baby",
                                                    0L);
                                            if (p1l <= 0L && p2l <= 0L) {
                                                if (p1.getLocation().distance(p2.getLocation()) <= 4) {
                                                    // play hearth particles via Bukkit
                                                    p1.getWorld().spawnParticle(Particle.HEART, p1.getLocation(), 15);
                                                    p2.getWorld().spawnParticle(Particle.HEART, p2.getLocation(), 15);

                                                    // set the current time in millis to the player data
                                                    DataMethods.setData("last-baby", System.currentTimeMillis(), p1);
                                                    DataMethods.setData("last-baby", System.currentTimeMillis(), p2);

                                                    // spawn baby

                                                    if (DataMethods.getSex(p1) == Sex.male) {
                                                        DataMethods.loadBabyVillager(true, p1, p2);
                                                    } else {
                                                        DataMethods.loadBabyVillager(true, p2, p1);
                                                    }

                                                    // tell message your baby grow up
                                                    MessageUtils.MessageParsedPlaceholders(p1,
                                                            new Message(
                                                                    MComesToLife.getMessages().getLang(
                                                                            "cmd.marry.procreate.success",
                                                                            "<prefix><gray>Tu pareja ha tenido un bebe con tigo</gray>")));
                                                } else {
                                                    // your spouse is too far
                                                    MessageUtils.MessageParsedPlaceholders(p1,
                                                            new Message(
                                                                    MComesToLife.getMessages().getLang(
                                                                            "cmd.marry.procreate.too-far",
                                                                            "<prefix><gray>Tu pareja esta muy lejos para procrear</gray>")));
                                                }
                                            } else if (p2l >= p2l + TimeUtils.getTimeOutProcreate()) {
                                                MessageUtils.MessageParsedPlaceholders(sender,
                                                        new Message(
                                                                MComesToLife.getMessages().getLang(
                                                                        "cmd.marry.procreate.timeout",
                                                                        "<prefix><gray>Tu pareja ya ha tenido un hijo recientemente</gray>")));

                                                Message timetoWait = new Message(
                                                        MComesToLife.getMessages().getLang("cmd.timeout",
                                                                "<prefix><gray>Debes Esperar <day_amount> <day_format> <hours_amount> <hours_format> <minutes_amount> <minutes_format> <seconds_amount> <seconds_format>/gray>"));
                                                Long currentTime = p2l + TimeUtils.getTimeOutProcreate()
                                                        - System.currentTimeMillis();
                                                timetoWait.setupTimePlaceholder(currentTime);
                                                MessageUtils.MessageParsedPlaceholders(sender, timetoWait);
                                                return true;
                                            } else if (p1l >= p2l + TimeUtils.getTimeOutProcreate()) {
                                                MessageUtils.MessageParsedPlaceholders(sender,
                                                        new Message(
                                                                MComesToLife.getMessages().getLang(
                                                                        "cmd.marry.procreate.timeout",
                                                                        "<prefix><gray>Tu yas ha tenido un hijo recientemente</gray>")));

                                                Message timetoWait = new Message(
                                                        MComesToLife.getMessages().getLang("cmd.timeout",
                                                                "<prefix><gray>Debes Esperar <day_amount> <day_format> <hours_amount> <hours_format> <minutes_amount> <minutes_format> <seconds_amount> <seconds_format>/gray>"));
                                                Long currentTime = p2l + TimeUtils.getTimeOutProcreate()
                                                        - System.currentTimeMillis();
                                                timetoWait.setupTimePlaceholder(currentTime);
                                                MessageUtils.MessageParsedPlaceholders(sender, timetoWait);
                                                return true;
                                            }

                                        }
                                    }

                                }
                            }
                        }

                        Player p = Bukkit.getPlayer(args[1]);
                        if (p != null) {
                            if (p.getUniqueId() == ((Player) sender).getUniqueId()) {
                                MessageUtils.MessageParsedPlaceholders(sender,
                                        new Message(
                                                MComesToLife.getMessages().getLang("cmd.marry.no-same",
                                                        "<prefix><gray>No puedes casarte contigo mismo</gray>")));
                                return true;
                            }
                            if (DataMethods.retrivePlayerData(p).containsKey("sex")
                                    && DataMethods.retrivePlayerData((Player) sender).containsKey("sex")) {
                                if (Sex.valueOf((String) DataMethods.retrivePlayerData(p).get("sex")) == Sex.valueOf(
                                        (String) DataMethods.retrivePlayerData((Player) sender).get("sex"))) {
                                    // our sex are the same
                                    MessageUtils.MessageParsedPlaceholders(sender,
                                            new Message(MComesToLife.getMessages()
                                                    .getLang("cmd.marry.same-gender",
                                                            "<prefix><gray>Sus generos no pueden ser los mismos</gray>")),
                                            p);
                                }
                            } else {
                                if (!DataMethods.retrivePlayerData((Player) sender).containsKey("sex")) {
                                    MessageUtils.MessageParsedPlaceholders(sender,
                                            new Message(MComesToLife.getMessages()
                                                    .getLang("cmd.marry.no-gender",
                                                            "<prefix><gray>No tienes ningun sexo establecido</gray>")),
                                            p);
                                    return true;
                                }
                                MessageUtils.MessageParsedPlaceholders(sender, new Message(MComesToLife.getMessages()
                                        .getLang("cmd.marry.no-gender-other",
                                                "<prefix><gray>El jugador <second_name> no tiene ningun sexo establecido</gray>")),
                                        p);
                                return true;
                            }
                            if (!DataMethods.getRelationMap(p).containsKey("spouse")
                                    && !DataMethods.getRelationMap((Player) sender).containsValue("spouse")) {
                                if (marryRequest.containsKey(p)) {
                                    MessageUtils.MessageParsedPlaceholders(sender,
                                            new Message(MComesToLife.getMessages().getLang("cmd.marry.already-request",
                                                    "<prefix><gray>El jugador <second_name> ya tiene una solicitud de matrimonio</gray>")),
                                            p);
                                } else {
                                    MessageUtils.MessageParsedPlaceholders(sender,
                                            new Message(MComesToLife.getMessages().getLang("cmd.marry.request",
                                                    "<prefix><gray>Has enviado una solicitud de matrimonio a <second_name></gray>")),
                                            p);
                                    MessageUtils.MessageParsedPlaceholders((CommandSender) p,
                                            new Message(MComesToLife.getMessages().getLang("cmd.marry.recive",
                                                    "<prefix><gray><second_name>Te ha solicitado matrimonio</gray>")),
                                            (Player) sender);
                                    marryRequest.put(p, (Player) sender);
                                }
                            } else {
                                MessageUtils.MessageParsedPlaceholders(sender,
                                        new Message(MComesToLife.getMessages().getLang("cmd.marry.already-married",
                                                "<prefix><gray>El jugador <second_name> ya esta casado</gray>")),
                                        p);
                            }
                        } else {
                            MessageUtils.MessageParsedPlaceholders(sender,
                                    new Message(MComesToLife.getMessages().getLang("cmd.marry.no-player",
                                            "<prefix><gray>El jugador <second_name> no esta conectado</gray>")),
                                    p);
                        }
                        if (args[1].equalsIgnoreCase("accept")) {
                            if (marryRequest.containsKey((Player) sender)) {
                                OfflinePlayer p2 = marryRequest.get((Player) sender);
                                if (p2.isOnline()) {
                                    MessageUtils.MessageParsedPlaceholders(sender,
                                            new Message(MComesToLife.getMessages().getLang("cmd.marry.accept",
                                                    "<prefix><gray>Has aceptado la solicitud de matrimonio de <second_name></gray>")),
                                            (Player) p2);
                                    MessageUtils.MessageParsedPlaceholders((CommandSender) p2,
                                            new Message(MComesToLife.getMessages().getLang("cmd.marry.accepted",
                                                    "<prefix><gray><second_name> Ha aceptado tu solicitud de matrimonio</gray>")),
                                            (Player) sender);
                                    DataMethods.marry((Player) sender, (Player) p2, 0);
                                    marryRequest.remove((Player) sender);
                                } else {
                                    MessageUtils.MessageParsedPlaceholders(sender,
                                            new Message(MComesToLife.getMessages().getLang("cmd.marry.offline",
                                                    "<prefix><gray>El jugador <second_name> esta desconectado</gray>")
                                                    .replace("<second_name>", p2.getName())
                                                    .replace("<second_displayname>", p2.getName())));
                                }
                            } else {
                                MessageUtils.MessageParsedPlaceholders(sender,
                                        new Message(MComesToLife.getMessages().getLang("cmd.marry.no-request",
                                                "<prefix><gray>No tienes ninguna solicitud de matrimonio</gray>")));
                            }
                        } else if (args[1].equalsIgnoreCase("deny")) {
                            if (marryRequest.containsKey((Player) sender)) {
                                OfflinePlayer p2 = marryRequest.get((Player) sender);
                                if (p2.isOnline()) {
                                    MessageUtils.MessageParsedPlaceholders((CommandSender) p2,
                                            new Message(MComesToLife.getMessages().getLang("cmd.marry.denied",
                                                    "<prefix><gray><second_name> Ha rechazado tu solicitud de matrimonio</gray>")),
                                            (Player) sender);
                                }
                                MessageUtils.MessageParsedPlaceholders(sender,
                                        new Message(MComesToLife.getMessages().getLang("cmd.marry.deny",
                                                "<prefix><gray>Has rechazado la solicitud de matrimonio de <second_name></gray>")
                                                .replace("<second_name>", p2.getName())
                                                .replace("<second_displayname>", p2.getName())));
                                marryRequest.remove((Player) sender);
                            } else {
                                MessageUtils.MessageParsedPlaceholders(sender,
                                        new Message(MComesToLife.getMessages().getLang("cmd.marry.no-request",
                                                "<prefix><gray>No tienes ninguna solicitud de matrimonio</gray>")));
                            }
                        } else {
                            MessageUtils.MessageParsedPlaceholders(sender,
                                    new Message(MComesToLife.getMessages().getLang("cmd.marry.no-args",
                                            "<prefix><gray>Debes especificar si quieres aceptar o rechazar la solicitud de matrimonio</gray> <nbsp><prefix><gray>O tambien para enviar una petición de matrimonio/<command> marry <player/accept/deny></gray>")));
                        }

                    }
                    case reload: {
                        try {
                            if (sender.hasPermission("mctl.admin.reload") || admin(sender)) {
                                MComesToLife.getPlugin().reloadConfig();
                                // send message reloaded
                                MessageUtils.MessageParsedPlaceholders(sender, new Message(MComesToLife.getMessages()
                                        .getLang("cmd.reload", "<prefix><gray>Has recargado el plugin</gray>")));
                            } else {
                                return true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case help:
                        break;
                    case fixVillager: {
                        // get looked entity from "Target" class utils
                        Entity e = Target.getTargetEntity((Player) sender);
                        if (Timers.entEnabled(e) && DataMethods.avaliable(e)) {
                            CustomVillager villager = new CustomVillager((LivingEntity) e);
                            villager.loadVillager(true);
                            MessageUtils.MessageParsedPlaceholders(sender, new Message(MComesToLife.getMessages()
                                    .getLang("cmd.fixvillager", "<prefix><gray>Has reparado al aldeano</gray>")));
                        }

                        return true;
                    }
                    case trade: {
                        EntityListener.playertoTrade.add(((Player) sender).getUniqueId().toString().toLowerCase());

                        return true;
                    }
                    case loadAllVillagers: {
                        WorldEntityLoader.makeVillagers();
                        MessageUtils.MessageParsedPlaceholders(sender, new Message(MComesToLife.getMessages()
                                .getLang("cmd.loadallvillagers",
                                        "<prefix><gray>Has recargado todos los aldeanos</gray>")));
                        return true;
                    }
                    case debug: {
                        if ((sender.hasPermission("mctl.cmd.debug") || admin(sender))) {
                            if (TypeAction.contains(args[1])) {
                                Entity e = Target.getTargetEntity((Player) sender);
                                if (DataMethods.isCustom(e)) {
                                    CustomVillager v = new CustomVillager((LivingEntity) e);
                                    v.loadVillager(false);
                                    v.getMood().speech(v, args[1], (Player) sender);
                                }
                            }
                        }
                        return true;
                    }
                    default:
                        break;

                }
            }
        } else {
            MessageUtils.MessageParsedPlaceholders(sender,
                    new Message(
                            MComesToLife.getMessages().getLang("cmd.help",
                                    "<prefix><gray>Los argumentos son muy cortos has /<command> help para ver los comandos</gray>")));
        }
        return true;
    }

}
