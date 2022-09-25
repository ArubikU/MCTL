package dev.arubik.mctl.holders;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.MComesToLife.timeFormat;
import dev.arubik.mctl.enums.sex;

public class Message {

    String string;
    @Setter
    Player player;

    HashMap<String, String> extraData = new HashMap<String, String>();

    public void ContraryformatSex(sex s) {
        if (s == sex.female) {
            replace("<contrary_sex_plural>",
                    MComesToLife.getMessages().getLang("message.gender.male.plural", "chicos"));
            replace("<contrary_sex_plural_pronoun>",
                    MComesToLife.getMessages().getLang("message.gender.male.pronoun", "los"));
            replace("<contrary_sex_pronoun>", MComesToLife.getMessages().getLang("message.gender.male.pronoun", "el"));
            replace("<contrary_sex_endchar>", MComesToLife.getMessages().getLang("message.gender.male.endchar", "o"));
            replace("<contrary_sex_endchardisapear>",
                    MComesToLife.getMessages().getLang("message.gender.male.endchardisapear", "endchardisapear"));
        } else {
            replace("<contrary_sex_plural>",
                    MComesToLife.getMessages().getLang("message.gender.female.plural", "chicas"));
            replace("<contrary_sex_plural_pronoun>",
                    MComesToLife.getMessages().getLang("message.gender.female.pronoun", "las"));
            replace("<contrary_sex_pronoun>",
                    MComesToLife.getMessages().getLang("message.gender.female.pronoun", "ella"));
            replace("<contrary_sex_endchar>", MComesToLife.getMessages().getLang("message.gender.female.endchar", "a"));
            replace("<contrary_sex_endchardisapear>",
                    MComesToLife.getMessages().getLang("message.gender.female.endchardisapear", "a"));
        }
    }

    public void formatSex(sex s) {
        if (s == sex.male) {
            replace("<villager_sex_plural>",
                    MComesToLife.getMessages().getLang("message.gender.male.plural", "chicos"));
            replace("<villager_sex_plural_pronoun>",
                    MComesToLife.getMessages().getLang("message.gender.male.pronoun", "los"));
            replace("<villager_sex_pronoun>", MComesToLife.getMessages().getLang("message.gender.male.pronoun", "el"));
            replace("<villager_sex_endchar>", MComesToLife.getMessages().getLang("message.gender.male.endchar", "o"));
            replace("<villager_sex_endchardisapear>",
                    MComesToLife.getMessages().getLang("message.gender.male.endchardisapear", "endchardisapear"));
        } else {
            replace("<villager_sex_plural>",
                    MComesToLife.getMessages().getLang("message.gender.female.plural", "chicas"));
            replace("<villager_sex_plural_pronoun>",
                    MComesToLife.getMessages().getLang("message.gender.female.pronoun", "las"));
            replace("<villager_sex_pronoun>",
                    MComesToLife.getMessages().getLang("message.gender.female.pronoun", "ella"));
            replace("<villager_sex_endchar>", MComesToLife.getMessages().getLang("message.gender.female.endchar", "a"));
            replace("<villager_sex_endchardisapear>",
                    MComesToLife.getMessages().getLang("message.gender.female.endchardisapear", "a"));
        }
    }

    public void formatPlayerSex(sex s) {
        if (s == sex.male) {
            replace("<player_sex_plural>",
                    MComesToLife.getMessages().getLang("message.gender.male.plural", "chicos"));
            replace("<player_sex_plural_pronoun>",
                    MComesToLife.getMessages().getLang("message.gender.male.pronoun", "los"));
            replace("<player_sex_pronoun>", MComesToLife.getMessages().getLang("message.gender.male.pronoun", "el"));
            replace("<player_sex_endchar>", MComesToLife.getMessages().getLang("message.gender.male.endchar", "o"));
        } else {
            replace("<player_sex_plural>",
                    MComesToLife.getMessages().getLang("message.gender.female.plural", "chicas"));
            replace("<player_sex_plural_pronoun>",
                    MComesToLife.getMessages().getLang("message.gender.female.pronoun", "las"));
            replace("<player_sex_pronoun>",
                    MComesToLife.getMessages().getLang("message.gender.female.pronoun", "ella"));
            replace("<player_sex_endchar>", MComesToLife.getMessages().getLang("message.gender.female.endchar", "a"));
        }
    }

    public void setupTimePlaceholder(Long time) {

        HashMap<timeFormat, Integer> timetoWaitMap = MComesToLife.getTimeFromDate(time);

        // day setup
        if (timetoWaitMap.get(timeFormat.DAY) == 0) {
            this.replace("<day_format> ", "");
            this.replace("<day_amount> ", "");
        }
        this.replace("<day_amount>", timetoWaitMap.get(timeFormat.DAY).toString());
        if (timetoWaitMap.get(timeFormat.DAY) <= 1) {
            this.replace("<day_format>", MComesToLife.getMessages().getLang("cmd.time.day", "dia"));
        } else {
            this.replace("<day_format>", MComesToLife.getMessages().getLang("cmd.time.days", "dias"));
        }

        // hours setup
        if (timetoWaitMap.get(timeFormat.HOURS) == 0) {
            this.replace("<hours_format> ", "");
            this.replace("<hour_amount> ", "");
        }
        this.replace("<hours_amount>", timetoWaitMap.get(timeFormat.HOURS).toString());
        if (timetoWaitMap.get(timeFormat.HOURS) <= 1) {
            this.replace("<hours_format>", MComesToLife.getMessages().getLang("cmd.time.hour", "hora"));
        } else {
            this.replace("<hours_format>", MComesToLife.getMessages().getLang("cmd.time.hours", "horas"));
        }

        // minutes setup
        if (timetoWaitMap.get(timeFormat.MINUTES) == 0) {
            this.replace("<minutes_format> ", "");
            this.replace("<minutes_amount> ", "");
        }
        this.replace("<minutes_amount>", timetoWaitMap.get(timeFormat.MINUTES).toString());
        if (timetoWaitMap.get(timeFormat.MINUTES) <= 1) {
            this.replace("<minutes_format>", MComesToLife.getMessages().getLang("cmd.time.minute", "minuto"));
        } else {
            this.replace("<minutes_format>", MComesToLife.getMessages().getLang("cmd.time.minutes", "minutos"));
        }

        // seconds setup
        if (timetoWaitMap.get(timeFormat.SECONDS) == 0) {
            this.replace("<seconds_format> ", "");
            this.replace("<seconds_amount> ", "");
        }
        this.replace("<seconds_amount>", timetoWaitMap.get(timeFormat.SECONDS).toString());
        if (timetoWaitMap.get(timeFormat.SECONDS) <= 1) {
            this.replace("<seconds_format>", MComesToLife.getMessages().getLang("cmd.time.second", "segundo"));
        } else {
            this.replace("<seconds_format>", MComesToLife.getMessages().getLang("cmd.time.seconds", "segundos"));
        }
    }

    public Message(String string) {
        this.string = string;
    }

    public Message(String string, HashMap<String, String> extraData) {
        this.string = string;
        this.extraData = extraData;
    }

    public void replace(String pattern, String replacement) {
        if (string.contains(pattern)) {
            this.string = string.replaceAll(pattern, replacement);
        }

    }

    public void replace(String[] patterns, String replacements) {
        for (int i = 0; i < patterns.length; i++) {
            if (string.contains(patterns[i])) {
                this.string = string.replaceAll(patterns[i], replacements);
            }
        }
    }

    public String getString() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, this.string);
        }

        return this.string;
    }

    public void removeLegacy() {
        this.replace("§f", "<white>");
        this.replace("§0", "<black>");
        this.replace("§1", "<dark_blue>");
        this.replace("§2", "<dark_green>");
        this.replace("§3", "<dark_aqua>");
        this.replace("§4", "<dark_red>");
        this.replace("§5", "<dark_purple>");
        this.replace("§6", "<gold>");
        this.replace("§7", "<gray>");
        this.replace("§8", "<dark_gray>");
        this.replace("§9", "<blue>");
        this.replace("§a", "<green>");
        this.replace("§b", "<aqua>");
        this.replace("§c", "<red>");
    }

    public Message removeMiniMessage() {
        this.replace("<white>", "§f");
        this.replace("<black>", "§0");
        this.replace("<dark_blue>", "§1");
        this.replace("<dark_green>", "§2");
        this.replace("<dark_aqua>", "§3");
        this.replace("<dark_red>", "§4");
        this.replace("<dark_purple>", "§5");
        this.replace("<gold>", "§6");
        this.replace("<gray>", "§7");
        this.replace("<dark_gray>", "§8");
        this.replace("<blue>", "§9");
        this.replace("<green>", "§a");
        this.replace("<aqua>", "§b");
        this.replace("<red>", "§c");
        this.replace("<light_purple>", "§d");
        this.replace("<yellow>", "§e");
        this.replace("<reset>", "§r");
        this.replace("<bold>", "§l");
        this.replace("<italic>", "§o");
        this.replace("<strikethrough>", "§m");
        this.replace("<underline>", "§n");
        this.replace("<obfuscated>", "§k");

        return this;
    }
}
