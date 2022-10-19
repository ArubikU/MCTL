package dev.arubik.mctl.placeholderApi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;

import dev.arubik.mctl.holders.Methods.DataMethods;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.util.function.BiFunction;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.arubik.mctl.MComesToLife;

public class MarryPlaceholder extends PlaceholderExpansion {
    String pid = "";
    private BiFunction<Player, String, String> onPlaceholderRequest;

    public MarryPlaceholder() {
        pid = "marry";
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
                }
            }
            if (identifier.equalsIgnoreCase("spouse_type")) {
                if (DataMethods.getSpouse((LivingEntity) player).isPresent()) {
                    if (DataMethods.getSpouse((LivingEntity) player).get() instanceof OfflinePlayer) {
                        return "PLAYER";
                    }
                    return DataMethods.getSpouse((LivingEntity) player).get().getType().toString();
                }
            }
            return "";
        });
    }

    public void setOnPlaceholderRequest(BiFunction<Player, String, String> onPlaceholderRequest) {
        this.onPlaceholderRequest = onPlaceholderRequest;
    }


    @Override
    public @NotNull String getAuthor() {
        return "Arubik";
    }

    @Override
    public @NotNull String getIdentifier() {
        return pid;
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
