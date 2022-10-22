package dev.arubik.mctl.placeholderApi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;

import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.Methods.DataMethods;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.util.function.BiFunction;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.arubik.mctl.MComesToLife;

public class MarryPlaceholder extends PlaceholderExpansion {
    private BiFunction<Player, String, String> onPlaceholderRequest;

    public MarryPlaceholder() {
        setOnPlaceholderRequest((player, identifier) -> {
            if (player == null) {
                return "";
            }
            if (identifier.equalsIgnoreCase("spouse")) {
                if (DataMethods.getSpouse((LivingEntity) player).isPresent()) {
                    if (DataMethods.getSpouse((LivingEntity) player).get() instanceof OfflinePlayer) {
                        return ((OfflinePlayer) DataMethods.getSpouse((LivingEntity) player).get()).getName();
                    }
                    return DataMethods.getSpouse((LivingEntity) player).get().getName();
                } else {
                    return "ANY";
                }
            }
            if (identifier.equalsIgnoreCase("spouse_type")) {
                if (DataMethods.getSpouse((LivingEntity) player).isPresent()) {
                    if (DataMethods.getSpouse((LivingEntity) player).get() instanceof OfflinePlayer) {
                        return "PLAYER";
                    }
                    return DataMethods.getSpouse((LivingEntity) player).get().getType().toString();
                } else {
                    return "ANY";
                }
            }
            if (identifier.equalsIgnoreCase("gender")) {
                return new Message("<player_sex>").formatPlayerSex(DataMethods.getSex(player)).getString();
            }
            if (identifier.equalsIgnoreCase("gender_pronoun")) {
                return new Message("<player_sex_pronoun>").formatPlayerSex(DataMethods.getSex(player)).getString();
            }
            if (identifier.equalsIgnoreCase("gender_sign")) {
                return new Message("<player_sign>").formatPlayerSex(DataMethods.getSex(player)).getString();
            }
            if (identifier.equalsIgnoreCase("gender_color")) {
                return new Message("<player_color>").formatPlayerSex(DataMethods.getSex(player)).getString();
            }
            return "";
        });
    }

    public void setOnPlaceholderRequest(BiFunction<Player, String, String> onPlaceholderRequest) {
        this.onPlaceholderRequest = onPlaceholderRequest;
    }

    public void registerTry() {
        if (!this.isRegistered()) {
            this.register();
        }
    }

    @Override
    public @NotNull String getAuthor() {
        return "Arubik";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "marry";
    }

    @Override
    public @NotNull String getVersion() {
        return MComesToLife.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    public String pch(String place) {
        return "%" + place + "%";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        return onPlaceholderRequest.apply(player, identifier);
    }
}
