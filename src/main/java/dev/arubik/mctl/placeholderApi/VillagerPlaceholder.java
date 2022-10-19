package dev.arubik.mctl.placeholderApi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;

import dev.arubik.mctl.holders.Methods.DataMethods;
import dev.arubik.mctl.utils.Target;
import dev.arubik.mctl.utils.GuiCreator.GuiHolder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.util.function.BiFunction;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;

public class VillagerPlaceholder extends PlaceholderExpansion {
    String pid = "";
    private BiFunction<Player, String, String> onPlaceholderRequest;

    public VillagerPlaceholder() {
        pid = "villager";
        setOnPlaceholderRequest((player, identifier) -> {
            if (player == null) {
                return "";
            }
            if (player.getOpenInventory() != null) {
                if (player.getOpenInventory().getTopInventory().getHolder() instanceof GuiHolder) {
                    GuiHolder holder = (GuiHolder) player.getOpenInventory().getTopInventory().getHolder();
                    if (holder.getInventoryData().containsKey("CUSTOMVILLAGER")) {

                        CustomVillager vil = (CustomVillager) holder.getInventoryData().get("CUSTOMVILLAGER");
                        vil.loadVillager(false);
                        if (identifier.equalsIgnoreCase("likes")) {
                            return String.valueOf(vil.getLikes(player).orElse(0));
                        }
                        if (identifier.equalsIgnoreCase("mood")) {
                            return String.valueOf(vil.getMood());
                        }
                        if (identifier.equalsIgnoreCase("hapiness")) {
                            return String.valueOf(vil.getHappiness().orElse(0));
                        }
                    }
                }
            }
            if (Target.getTargetEntity((LivingEntity) player) != null) {
                if (DataMethods.avaliable(Target.getTargetEntity((LivingEntity) player))) {
                    CustomVillager vil = new CustomVillager(
                            (LivingEntity) Target.getTargetEntity((LivingEntity) player));
                    vil.loadVillager(false);
                    if (identifier.equalsIgnoreCase("likes")) {
                        return String.valueOf(vil.getLikes(player).orElse(0));
                    }
                    if (identifier.equalsIgnoreCase("mood")) {
                        return String.valueOf(vil.getMood());
                    }
                    if (identifier.equalsIgnoreCase("hapiness")) {
                        return String.valueOf(vil.getHappiness().orElse(0));
                    }
                }
            }
            if (DataMethods.getlastClickedEntity(player) != null) {
                CustomVillager vil = DataMethods.getlastClickedEntity(player);
                if (identifier.equalsIgnoreCase("likes")) {
                    return String.valueOf(vil.getLikes(player).orElse(0));
                }
                if (identifier.equalsIgnoreCase("mood")) {
                    return String.valueOf(vil.getMood());
                }
                if (identifier.equalsIgnoreCase("hapiness")) {
                    return String.valueOf(vil.getHappiness().orElse(0));
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
