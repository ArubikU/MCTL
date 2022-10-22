package dev.arubik.mctl.placeholderApi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;

import dev.arubik.mctl.holders.Message;
import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.MessageUtils;
import dev.arubik.mctl.utils.Target;
import dev.arubik.mctl.utils.GuiCreator.GuiHolder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.util.function.BiFunction;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;

public class VillagerPlaceholder extends PlaceholderExpansion {
    private BiFunction<Player, String, String> onPlaceholderRequest;

    public VillagerPlaceholder() {
        setOnPlaceholderRequest((player, identifier) -> {
            if (player == null) {
                return "";
            }

            CustomVillager vil = null;
            if (Target.getTargetEntity((LivingEntity) player) != null) {
                if (DataMethods.avaliable(Target.getTargetEntity((LivingEntity) player))) {
                    vil = new CustomVillager(
                            (LivingEntity) Target.getTargetEntity((LivingEntity) player));
                }
            }
            if (DataMethods.getlastClickedEntity(player) != null) {
                vil = DataMethods.getlastClickedEntity(player);
            }
                        if (player.getOpenInventory() != null) {
                if (player.getOpenInventory().getTopInventory().getHolder() instanceof GuiHolder) {
                    GuiHolder holder = (GuiHolder) player.getOpenInventory().getTopInventory().getHolder();
                    if (holder.getInventoryData().containsKey("CUSTOMVILLAGER")) {
                        vil = (CustomVillager) holder.getInventoryData().get("CUSTOMVILLAGER");
                    }
                }
            }
            
            if(identifier.toLowerCase().startsWith("parse")){
                return MessageUtils.StringParsedPlaceholders(player, new Message(identifier.split(",")[0]), vil);
            }
            if(vil==null){
                return "null";
            }
            vil.loadVillager(false);
            if (identifier.equalsIgnoreCase("likes")) {
                return String.valueOf(vil.getLikes(player).orElse(0));
            }
            if (identifier.equalsIgnoreCase("mood")) {
                return String.valueOf(vil.getMood());
            }
            if (identifier.equalsIgnoreCase("trait")) {
                return String.valueOf(vil.getTrait());
            }
            if (identifier.equalsIgnoreCase("hapiness")) {
                return String.valueOf(vil.getHappiness().orElse(0));
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
        return "villager";
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
