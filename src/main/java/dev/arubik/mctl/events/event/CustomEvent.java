package dev.arubik.mctl.events.event;

import java.util.HashMap;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.enums.EventType;
import dev.arubik.mctl.utils.MultiThings.BiObject;
import dev.arubik.mctl.utils.MultiThings.ForEachArrays;
import lombok.Getter;
import lombok.Setter;

public class CustomEvent extends org.bukkit.event.Event implements Cancellable {

    @Getter
    @Setter
    Player player;
    @Getter
    @Setter
    @Nullable
    dev.arubik.mctl.entity.CustomVillager CustomVillager;

    @Getter
    HashMap<String, Object> params = new HashMap<String, Object>();

    private Boolean cancelled = false;

    public CustomEvent(Player player, dev.arubik.mctl.entity.CustomVillager CustomVillager) {
        this.CustomVillager = CustomVillager;
        this.player = player;
    }

    public CustomEvent(Player player) {
        this.CustomVillager = null;
        this.player = player;
    }

    public CustomEvent(dev.arubik.mctl.entity.CustomVillager CustomVillager) {
        this.CustomVillager = CustomVillager;
        this.player = null;
    }

    public CustomEvent(Player player, dev.arubik.mctl.entity.CustomVillager customVillager,
            BiObject<String, Object>... params) {
        this(player, customVillager);
        ForEachArrays array = new ForEachArrays(params);
        array.forEachArray(param -> {
            BiObject<String, Object> paramData = (BiObject<String, Object>) param;
            this.params.put(paramData.getFirstValue(), paramData.getSecondValue());
        });
    }

    public CustomEvent(Player player, BiObject<String, Object>... params) {
        this(player);
        ForEachArrays array = new ForEachArrays(params);
        array.forEachArray(param -> {
            BiObject<String, Object> paramData = (BiObject<String, Object>) param;
            this.params.put(paramData.getFirstValue(), paramData.getSecondValue());
        });
    }

    public CustomEvent(dev.arubik.mctl.entity.CustomVillager customVillager, BiObject<String, Object>... params) {
        this(customVillager);
        ForEachArrays array = new ForEachArrays(params);
        array.forEachArray(param -> {
            BiObject<String, Object> paramData = (BiObject<String, Object>) param;
            this.params.put(paramData.getFirstValue(), paramData.getSecondValue());
        });
    }

    public void putParams(BiObject<String, Object>... params) {
        ForEachArrays array = new ForEachArrays(params);
        array.forEachArray(param -> {
            BiObject<String, Object> paramData = (BiObject<String, Object>) param;
            this.params.put(paramData.getFirstValue(), paramData.getSecondValue());
        });
    }

    public void putParam(BiObject<String, Object> param) {
        this.params.put(param.getFirstValue(), param.getSecondValue());
    }

    public void putParam(String a, Object b) {
        this.params.put(a, b);
    }

    public EventType getType() {
        EventType a = EventType.NULL;
        for (String key : this.params.keySet()) {
            if (key.equalsIgnoreCase("TYPE")) {
                a = EventType.getType((this.params.get(key)).toString());
            }
        }
        return a;
    }

    public void Invoke() {
        //this.callEvent();
    }

    public boolean existVillager() {
        return (CustomVillager != null);
    }

    public boolean existPlayer() {
        return (player != null);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        cancelled = true;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return new HandlerList();
    }

}
