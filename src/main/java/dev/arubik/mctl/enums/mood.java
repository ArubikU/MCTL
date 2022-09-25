package dev.arubik.mctl.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.events.event.CustomEvent;
import dev.arubik.mctl.holders.EntityAi;
import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.VillagerInventoryHolder;
import dev.arubik.mctl.holders.WaitTimePerAction;
import dev.arubik.mctl.holders.timers;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.CustomConfigurationSection;
import dev.arubik.mctl.utils.fileUtils;
import dev.arubik.mctl.utils.messageUtils;

public enum mood {
    HAPPY,
    NEUTRAL,
    SADNESS,
    ANGER,
    FATIGUE;

    public static Boolean contains(String arg) {
        for (mood arg0 : mood.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    private static WaitTimePerAction waitTimes = new WaitTimePerAction("interact");

    public static HashMap<String, String> sameAction = new HashMap<String, String>();

    public static int rand(int min, int max) {
        return (new Random()).nextInt(max - min + 1) + min;
    }

    public static String getText(String type, String family) {
        String message = "null";
        Bukkit.getConsoleSender().sendMessage("type: " + type.toLowerCase());
        List<String> talkList = MComesToLife.getVillagers()
                .getLangList("speech" + family + "." + type.toLowerCase(), new ArrayList<String>());
        int index = rand(0, talkList.size() - 1);
        message = talkList.get(index);
        return message;
    }

    private boolean sameCheck(LivingEntity p, String type) {
        if (mood.sameAction.containsKey(p.getUniqueId().toString())) {
            if (((String) mood.sameAction.get(p.getUniqueId().toString())).contains(type)) {
                int times = Integer.parseInt(((String) mood.sameAction.get(p.getUniqueId().toString())).split(":")[1])
                        + 1;
                mood.sameAction.put(p.getUniqueId().toString(), String.valueOf(type) + ":" + times);

                if (times >= MComesToLife.config.getInt("config.interactions.max", 6))
                    return true;
            } else {
                mood.sameAction.put(p.getUniqueId().toString(), String.valueOf(type) + ":1");
            }
        } else {
            mood.sameAction.put(p.getUniqueId().toString(), String.valueOf(type) + ":1");
        }
        return false;
    }

    public static List<String> playersToInteract = new ArrayList<String>();

    public void speech(CustomVillager cv, String type, LivingEntity interacter) {
        CustomEvent event = new CustomEvent((Player) interacter, cv);
        event.putParam("TYPE", EventType.INTERACTION);
        event.putParam("TYPEACTION", type);
        event.Invoke();
        if (event.isCancelled())
            return;
        if (waitTimes.Enabled()) {
            Long timeToP = MComesToLife.getTimeFromConfig(new CustomConfigurationSection(
                    MComesToLife.config.getConfig().getConfigurationSection("config.time-interact")));
            if (!waitTimes.able((Player) interacter, timeToP)) {

                Message timetoWait = new Message(
                        MComesToLife.getMessages().getLang("cmd.timeout",
                                "<prefix><gray>Debes Esperar <day_amount> <day_format> <hours_amount> <hours_format> <minutes_amount> <minutes_format> <seconds_amount> <seconds_format>/gray>"));
                timetoWait.setupTimePlaceholder(timeToP);
                messageUtils.MessageParsedPlaceholders((Player) interacter, timetoWait);

                return;
            }
            waitTimes.addToTime((Player) interacter);
        }

        if (playersToInteract.contains(interacter.getUniqueId().toString())) {
            Bukkit.getScheduler().runTaskLater(MComesToLife.plugin, new Runnable() {
                @Override
                public void run() {
                    if (playersToInteract.contains(interacter.getUniqueId().toString())) {
                        playersToInteract.remove(interacter.getUniqueId().toString());
                    }
                }

            }, 0);
            playersToInteract.remove(interacter.getUniqueId().toString());
            return;
        }
        playersToInteract.add(interacter.getUniqueId().toString());

        if (type.equalsIgnoreCase("PROCREATE")) {
            Long p1l = (Long) DataMethods.retrivePlayerData((Player) interacter)
                    .getOrDefault("last-baby", 0L);
            Long p2l = (Long) DataMethods.retriveData(cv.getLivingEntity()).getOrDefault("last-baby",
                    0L);
            if (p1l <= 0L && p2l <= 0L) {
                if (interacter.getLocation().distance(cv.getLivingEntity().getLocation()) <= 4) {
                    // play hearth particles via Bukkit
                    interacter.getWorld().spawnParticle(Particle.HEART, interacter.getLocation(), 15);
                    cv.getLivingEntity().getWorld().spawnParticle(Particle.HEART, cv.getLivingEntity().getLocation(),
                            15);

                    // set the current time in millis to the player data
                    DataMethods.setData("last-baby", System.currentTimeMillis(), interacter);
                    DataMethods.setData("last-baby", System.currentTimeMillis(), cv.getLivingEntity());

                    // spawn baby

                    messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(
                            this.getText("procreate-yes",
                                    DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                            cv);
                    Bukkit.getScheduler().runTaskLater(MComesToLife.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            if (DataMethods.getSex((Player) interacter) == sex.male) {
                                DataMethods.loadBabyVillager(true, interacter, cv.getLivingEntity());
                            } else {
                                DataMethods.loadBabyVillager(true, cv.getLivingEntity(), interacter);
                            }

                            // tell message your baby grow up
                            messageUtils.MessageParsedPlaceholders((CommandSender) interacter,
                                    new Message(
                                            MComesToLife.getMessages().getLang(
                                                    "cmd.marry.procreate.success",
                                                    "<prefix><gray>Tu pareja ha tenido un bebe con tigo</gray>")));
                        }

                    }, 4L);
                } else {
                    // your spouse is too far
                    messageUtils.MessageParsedPlaceholders((CommandSender) interacter,
                            new Message(
                                    MComesToLife.getMessages().getLang(
                                            "cmd.marry.procreate.too-far",
                                            "<prefix><gray>Tu pareja esta muy lejos para procrear</gray>")));
                }
            } else if (p2l >= p2l + MComesToLife.getTimeOutProcreate()) {
                messageUtils.MessageParsedPlaceholders((CommandSender) interacter,
                        new Message(
                                MComesToLife.getMessages().getLang(
                                        "cmd.marry.procreate.timeout",
                                        "<prefix><gray>Tu pareja ya ha tenido un hijo recientemente</gray>")));

                Message timetoWait = new Message(
                        MComesToLife.getMessages().getLang("cmd.timeout",
                                "<prefix><gray>Debes Esperar <day_amount> <day_format> <hours_amount> <hours_format> <minutes_amount> <minutes_format> <seconds_amount> <seconds_format>/gray>"));
                Long currentTime = p2l + MComesToLife.getTimeOutProcreate()
                        - System.currentTimeMillis();
                timetoWait.setupTimePlaceholder(currentTime);
                messageUtils.MessageParsedPlaceholders((CommandSender) interacter, timetoWait);
            } else if (p1l >= p2l + MComesToLife.getTimeOutProcreate()) {
                messageUtils.MessageParsedPlaceholders((CommandSender) interacter,
                        new Message(
                                MComesToLife.getMessages().getLang(
                                        "cmd.marry.procreate.timeout",
                                        "<prefix><gray>Tu yas ha tenido un hijo recientemente</gray>")));

                Message timetoWait = new Message(
                        MComesToLife.getMessages().getLang("cmd.timeout",
                                "<prefix><gray>Debes Esperar <day_amount> <day_format> <hours_amount> <hours_format> <minutes_amount> <minutes_format> <seconds_amount> <seconds_format>/gray>"));
                Long currentTime = p2l + MComesToLife.getTimeOutProcreate()
                        - System.currentTimeMillis();
                timetoWait.setupTimePlaceholder(currentTime);
                messageUtils.MessageParsedPlaceholders((CommandSender) interacter, timetoWait);

            }

        }VillagerInventoryHolder vil = VillagerInventoryHolder.getInstance(cv);
        vil.loadInventory();

        if (type.equalsIgnoreCase("GIFT")) {
            
            vil.giveItem((Player) interacter);
        }
        if (type.equalsIgnoreCase("UNEQUIPHAT")) {
            
            vil.deEquip(EquipmentSlot.HEAD);
        }
        if (type.equalsIgnoreCase("UNEQUIPCHEST")) {
            
            vil.deEquip(EquipmentSlot.CHEST);
        }
        if (type.equalsIgnoreCase("UNEQUILEGS")) {
            
            vil.deEquip(EquipmentSlot.LEGS);
        }
        if (type.equalsIgnoreCase("UNEQUIPBOOTS")) {
            
            vil.deEquip(EquipmentSlot.FEET);
        }
        if (type.equalsIgnoreCase("UNEQUIPHAND")) {
            
            vil.deEquip(EquipmentSlot.HAND);
        }
        if (type.equalsIgnoreCase("UNEQUIPOFFHAND")) {
            
            vil.deEquip(EquipmentSlot.OFF_HAND);
        }

        if (type.equalsIgnoreCase("follow")) {
            messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(
                    this.getText("follow", DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                    cv);
            cv.getLivingEntity().removePotionEffect(PotionEffectType.SLOW);
            EntityAi.follow((Mob) cv.getLivingEntity(), (Player) interacter);
            return;
        } else if (type.equalsIgnoreCase("stop")) {
            try {
                EntityAi.stopFollow((Mob) cv.getLivingEntity());
                return;
            } catch (Exception exception) {
            }
        } else if (type.equalsIgnoreCase("stay")) {
            cv.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 998001, 257));
            return;
        } else if (type.equalsIgnoreCase("move")) {
            cv.getLivingEntity().removePotionEffect(PotionEffectType.SLOW);
            return;
        } else if (type.equalsIgnoreCase("SETHOME")) {
            EntityAi.SetHome((Mob) cv.getLivingEntity(), cv.getLivingEntity().getLocation());
            return;
        } else if (type.equalsIgnoreCase("GOHOME")) {
            cv.getLivingEntity().removePotionEffect(PotionEffectType.SLOW);
            EntityAi.goHome((Mob) cv.getLivingEntity());
            return;
        }

        try {
            int hearts = cv.getHappiness().orElse(1);
            int min = 1;
            int max = 6;

            if (cv.getLivingEntity() instanceof Villager
                    && ((Villager) cv.getLivingEntity()).getProfession().toString().equalsIgnoreCase("NITWIT")) {
                max -= 5;
            }
            boolean likes = false;
            if (cv.getLikes(interacter).orElse(0) >= 10) {
                likes = true;
            }
            String trait = cv.getData().get("trait").toString();

            if (sameCheck(interacter, type)) {
                max += 6;
            }
            if (type.equalsIgnoreCase("story") || type.equalsIgnoreCase("chat") || type.equalsIgnoreCase("joke")
                    || type.equalsIgnoreCase("play")) {

                if (trait.equalsIgnoreCase("Shy"))
                    max++;
                if (trait.equalsIgnoreCase("Fun") && (type.equalsIgnoreCase("joke") || type.equalsIgnoreCase("play"))) {
                    max -= 2;
                } else if (trait.equalsIgnoreCase("Fun")) {
                    max--;
                }
                if (trait.equalsIgnoreCase("Outgoing")) {
                    max++;
                }
                if (hearts > 20)
                    max--;
                if (hearts > 50)
                    max--;
                if (hearts > 80)
                    max--;
                if (hearts > 150)
                    max--;
                if (max < 1) {
                    max = 1;
                }
                int r = (new Random()).nextInt(max - min + 1) + min;
                if (r == 1) {
                    messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(this
                            .getText(type + "-good",
                                    DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                            cv);
                    cv.addLikes(rand(1, 10), interacter);
                } else {
                    messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(this
                            .getText(type + "-bad",
                                    DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                            cv);
                    cv.takeLikes(rand(1, 10), interacter);
                }
            } else if (type.equalsIgnoreCase("greet")) {

                if (trait.equalsIgnoreCase("Shy"))
                    max++;
                if (trait.equalsIgnoreCase("Friendly"))
                    max -= 2;
                if (trait.equalsIgnoreCase("Fun"))
                    max++;
                if (trait.equalsIgnoreCase("Outgoing"))
                    max++;
                if (trait.equalsIgnoreCase("Serious")) {
                    max++;
                }
                if (hearts > 10)
                    max--;
                if (hearts > 30)
                    max--;
                if (hearts > 50)
                    max--;
                if (hearts > 100)
                    max--;
                if (hearts > 300)
                    max--;
                if (max < 1)
                    max = 1;
                int r = (new Random()).nextInt(max - min + 1) + min;
                if (r == 1) {
                    messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(this
                            .getText(type + "-good",
                                    DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                            cv);
                    cv.addLikes(rand(1, 10), interacter);
                } else {
                    messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(this
                            .getText(type + "-bad",
                                    DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                            cv);
                    cv.takeLikes(rand(1, 10), interacter);
                }
            } else if (type.equalsIgnoreCase("flirt")) {

                if (likes) {
                    max -= 4;
                }
                if (trait.equalsIgnoreCase("Shy")
                        && !(DataMethods.getFamily("", (Player) interacter, cv.getLivingEntity())
                                .equalsIgnoreCase("spouse")))
                    max += 2;
                if (trait.equalsIgnoreCase("Irritable"))
                    max++;
                if (trait.equalsIgnoreCase("Emotional"))
                    max += rand(-1, 1);
                if (trait.equalsIgnoreCase("Outgoing"))
                    max--;
                if (trait.equalsIgnoreCase("Serious")) {
                    max++;
                }
                if (hearts < -10)
                    max++;
                if (hearts < -30)
                    max++;
                if (hearts < -50)
                    max++;
                if (hearts > 30)
                    max--;
                if (hearts > 50)
                    max--;
                if (hearts > 80)
                    max--;
                if (hearts > 100)
                    max--;
                if (hearts > 300)
                    max--;
                if (max < 1) {
                    max = 1;
                }
                int r = (new Random()).nextInt(max - min + 1) + min;
                if (r == 1) {
                    messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(this
                            .getText(type + "-good",
                                    DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                            cv);
                    cv.addLikes(rand(1, 10), interacter);
                } else {
                    messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(this
                            .getText(type + "-bad",
                                    DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                            cv);
                    cv.takeLikes(rand(1, 10), interacter);
                }
            } else if (type.equalsIgnoreCase("kiss")) {

                if (likes) {
                    max -= 2;
                }
                if (trait.equalsIgnoreCase("Shy")
                        && !(DataMethods.getFamily("", (Player) interacter, cv.getLivingEntity())
                                .equalsIgnoreCase("spouse")))
                    max += 3;
                if (trait.equalsIgnoreCase("Irritable"))
                    max += 2;
                if (trait.equalsIgnoreCase("Emotional"))
                    max += rand(-3, 3);
                if (trait.equalsIgnoreCase("Outgoing"))
                    max += rand(-1, 3);
                if (trait.equalsIgnoreCase("Serious") && hearts < 50) {
                    max++;
                }
                if (hearts < 30)
                    max++;
                if (hearts < 20)
                    max += 2;
                if (hearts < 10)
                    max += 3;
                if (hearts < -5)
                    max -= 2;
                if (hearts < -20)
                    max += 6;
                if (hearts > 50)
                    max--;
                if (hearts > 80)
                    max--;
                if (hearts > 100)
                    max--;
                if (hearts > 250)
                    max--;
                if (hearts > 400) {
                    max--;
                }
                if (DataMethods.getFamily("", (Player) interacter, cv.getLivingEntity()).equalsIgnoreCase("spouse"))
                    max -= 3;
                if (max < 1) {
                    max = 1;
                }
                int r = (new Random()).nextInt(max - min + 1) + min;
                if (r == 1) {
                    messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(this
                            .getText(type + "-good",
                                    DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                            cv);
                    cv.addLikes(rand(5, 15), interacter);
                } else {
                    messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(this
                            .getText(type + "-bad",
                                    DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                            cv);
                    cv.takeLikes(rand(5, 15), interacter);
                }
            } else if (type.equalsIgnoreCase("insult")) {
                messageUtils.MessageParsedPlaceholders((CommandSender) ((Player) interacter), new Message(
                        this.getText("insult", DataMethods.getFamily(type, (Player) interacter, cv.getLivingEntity()))),
                        cv);
                cv.takeLikes(rand(1, 15), interacter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
