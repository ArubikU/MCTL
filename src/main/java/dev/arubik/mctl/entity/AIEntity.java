package dev.arubik.mctl.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection.Method;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.holders.Methods.DataMethods;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.abstraction.ChipUtil;
import me.gamercoder215.mobchip.abstraction.ChipUtil1_18_R1;
import me.gamercoder215.mobchip.ai.memories.Memory;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;

public class AIEntity extends CustomEntity {
    public AIEntity(LivingEntity v) {
        super(v);
    }

    public LivingEntity getLivingEntity() {
        return (LivingEntity) villager;
    }

    public EntityBrain getBrain() {
        return BukkitBrain.getBrain((Mob) villager);
    }

    public void setMemoryTime(Memory memory, Object value, long time) {
        getBrain().setMemory(memory, value, time);
    }

    public void setMemory(Memory memory, Object value) {
        getBrain().setMemory(memory, value);
    }

    public <T> @Nullable T getMemory(Memory<T> memory) {
        return getBrain().getMemory(memory);
    }

    public void setNBT(Memory memory, Object value) {
        getBrain().getNBTEditor().set(memory.getKey().toString(), value);
    }

    public void setNBTTime(Memory memory, Object value, long time) {
        getBrain().getNBTEditor().set(memory.getKey().toString(), value);
        Bukkit.getScheduler().runTaskLater(MComesToLife.plugin, () -> {
            getBrain().getNBTEditor().remove(memory.getKey().toString());
        }, time);
    }

    public <T> @Nullable T getNBT(Memory<T> memory) {
        String key = memory.getKey().toString();
        if (getBrain().getNBTEditor().getString(key) == null)
            return null;
        switch (memory.getBukkitClass().getSimpleName()) {
            case "Integer":
                return (T) (Integer) getBrain().getNBTEditor().getInteger(key);
            case "Double":
                return (T) (Double) getBrain().getNBTEditor().getDouble(key);
            case "Float":
                return (T) (Float) getBrain().getNBTEditor().getFloat(key);
            case "Long":
                return (T) (Long) getBrain().getNBTEditor().getLong(key);
            case "Byte":
                return (T) (Byte) getBrain().getNBTEditor().getByte(key);
            case "String":
                return (T) getBrain().getNBTEditor().getString(key);
            case "ItemStack":
                return (T) getBrain().getNBTEditor().getItemStack(key);
            default:
                return null;
        }
    }

    public void removeNBT(Memory memory) {
        getBrain().getNBTEditor().remove(memory.getKey().toString());
    }

    public Boolean canSee(Mob m) {
        return getBrain().canSee(m);
    }

    @Nullable
    public net.minecraft.world.entity.LivingEntity getNMSEntity() {
        try {

            Field f = getBrain().getBody().getClass().getDeclaredField("nmsMob");
            f.setAccessible(true);

            java.lang.reflect.Method toNMS = null;
            for (java.lang.reflect.Method m : ChipUtil.getWrapper().getClass().getDeclaredMethods()) {
                if (m.getName().toLowerCase().contains("tonms")) {
                    toNMS = m;
                }
            }
            if (toNMS == null) {
                toNMS.setAccessible(true);
                return ((net.minecraft.world.entity.LivingEntity) ((net.minecraft.world.entity.Mob) toNMS.invoke(
                        ChipUtil.getWrapper(),
                        (Mob) this.villager)));
            } else {
                if (f.get(null) != null) {
                    return ((net.minecraft.world.entity.LivingEntity) ((net.minecraft.world.entity.Mob) f.get(null)));
                }
            }

            return null;
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
