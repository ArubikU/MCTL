package dev.arubik.mctl.placeholderApi;

import java.util.function.BiFunction;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.arubik.mctl.MComesToLife;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderBase extends PlaceholderExpansion {
    String pid = "";
    private BiFunction<Player, String, String> onPlaceholderRequest;

    protected PlaceholderBase(String identifier) {
        this.pid = identifier;
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
