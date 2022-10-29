package dev.arubik.mctl.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import dev.arubik.mctl.MComesToLife;
import lombok.Getter;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.abstraction.ChipUtil;
import me.gamercoder215.mobchip.ai.memories.Memory;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import me.gamercoder215.mobchip.nbt.EntityNBT;

public class AIEntity extends CustomEntity {
    public AIEntity(LivingEntity v) {
        super(v);
        this.Brain = generateBrain();
        this.nMSEntity = generateNMS();
        this.NBTEditor = Brain.getNBTEditor();
    }

    @Getter
    private EntityNBT NBTEditor = null;
    @Getter
    private EntityBrain Brain = null;

    public static AIEntity getFromEntity(LivingEntity v) {
        return new AIEntity(v);
    }

    public EntityBrain generateBrain() {
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
        NBTEditor.set(memory.getKey().toString(), value);
    }

    public void setNBTTime(Memory memory, Object value, long time) {
        if (!MComesToLife.getPlugin().isEnabled()) {
            NBTEditor.remove(memory.getKey().toString());
            return;
        }
        NBTEditor.set(memory.getKey().toString(), value);
        Bukkit.getScheduler().runTaskLater(MComesToLife.getPlugin(), () -> {
            NBTEditor.remove(memory.getKey().toString());
        }, time);
    }

    public <T> @Nullable T getNBT(Memory<T> memory) {
        String key = memory.getKey().toString();
        if (NBTEditor.getString(key) == null)
            return null;
        switch (memory.getBukkitClass().getSimpleName()) {
            case "Integer":
                return (T) (Integer) NBTEditor.getInteger(key);
            case "Double":
                return (T) (Double) NBTEditor.getDouble(key);
            case "Float":
                return (T) (Float) NBTEditor.getFloat(key);
            case "Long":
                return (T) (Long) NBTEditor.getLong(key);
            case "Byte":
                return (T) (Byte) NBTEditor.getByte(key);
            case "String":
                return (T) NBTEditor.getString(key);
            case "ItemStack":
                return (T) NBTEditor.getItemStack(key);
            default:
                return null;
        }
    }

    public void removeNBT(Memory memory) {
        NBTEditor.remove(memory.getKey().toString());
    }

    public Boolean canSee(Mob m) {
        return getBrain().canSee(m);
    }

    public void putContainerData(NamespacedKey tag, Object object) {
        if (object.getClass().getName().equals("java.lang.String")) {
            this.villager.getPersistentDataContainer().set(
                    tag, PersistentDataType.STRING,
                    (String) object);
        } else if (object.getClass().getName().equals("java.lang.Integer")) {
            this.villager.getPersistentDataContainer().set(
                    tag, PersistentDataType.INTEGER,
                    (Integer) object);
        } else if (object.getClass().getName().equals("java.lang.Double")) {
            this.villager.getPersistentDataContainer().set(
                    tag, PersistentDataType.DOUBLE,
                    (Double) object);
        } else if (object.getClass().getName().equals("java.lang.Float")) {
            this.villager.getPersistentDataContainer().set(
                    tag, PersistentDataType.FLOAT,
                    (Float) object);
        } else if (object.getClass().getName().equals("java.lang.Long")) {
            this.villager.getPersistentDataContainer().set(
                    tag, PersistentDataType.LONG,
                    (Long) object);
        } else if (object.getClass().getName().equals("java.lang.Short")) {
            this.villager.getPersistentDataContainer().set(
                    tag, PersistentDataType.SHORT,
                    (Short) object);
        } else if (object.getClass().getName().equals("java.lang.Byte")) {
            this.villager.getPersistentDataContainer().set(
                    tag, PersistentDataType.BYTE,
                    (Byte) object);
        } else if (object.getClass().getName().equals("java.lang.Boolean")) {
            this.villager.getPersistentDataContainer().set(
                    tag, PersistentDataType.STRING,
                    object.toString());
        }
    }

    public void putContainerData(Memory memory, Object object) {
        putContainerData(memory.getKey(), object);
    }

    public void putContainerData(String tag, Object object) {
        putContainerData(new NamespacedKey(MComesToLife.getPlugin(), tag), object);
    }

    public void putContainerDataTime(NamespacedKey tag, Object object, long time) {
        putContainerData(tag, object);
        Bukkit.getScheduler().runTaskLater(MComesToLife.getPlugin(), () -> {
            removeContainerData(tag);
        }, time);
    }

    public void putContainerDataTime(Memory memory, Object object, long time) {
        putContainerDataTime(memory.getKey(), object, time);
    }

    public void putContainerDataTime(String tag, Object object, long time) {
        putContainerDataTime(new NamespacedKey(MComesToLife.getPlugin(), tag), object, time);
    }

    public Boolean containsContainerData(NamespacedKey tag) {
        return this.villager.getPersistentDataContainer()
                .has(tag);
    }

    public Boolean containsContainerData(Memory memory) {
        return containsContainerData(memory.getKey());
    }

    public Boolean containsContainerData(String tag) {
        return containsContainerData(new NamespacedKey(MComesToLife.getPlugin(), tag));
    }

    public <T> T getContainerData(NamespacedKey tag, Class<T> Type) {
        if (Type.getName().equals("java.lang.String")) {
            return (T) this.villager.getPersistentDataContainer().get(
                    tag, PersistentDataType.STRING);
        } else if (Type.getName().equals("java.lang.Integer")) {
            return (T) this.villager.getPersistentDataContainer().get(
                    tag, PersistentDataType.INTEGER);
        } else if (Type.getName().equals("java.lang.Double")) {
            return (T) this.villager.getPersistentDataContainer().get(
                    tag, PersistentDataType.DOUBLE);
        } else if (Type.getName().equals("java.lang.Float")) {
            return (T) this.villager.getPersistentDataContainer().get(
                    tag, PersistentDataType.FLOAT);
        } else if (Type.getName().equals("java.lang.Long")) {
            return (T) this.villager.getPersistentDataContainer()
                    .get(tag, PersistentDataType.LONG);
        } else if (Type.getName().equals("java.lang.Short")) {
            return (T) this.villager.getPersistentDataContainer().get(
                    tag, PersistentDataType.SHORT);
        } else if (Type.getName().equals("java.lang.Byte")) {
            return (T) this.villager.getPersistentDataContainer()
                    .get(tag, PersistentDataType.BYTE);
        } else if (Type.getName().equals("java.lang.Boolean")) {
            return (T) Boolean.valueOf(this.villager.getPersistentDataContainer()
                    .get(tag,
                            PersistentDataType.STRING));
        }
        return null;
    }

    public <T> T getContainerData(String tag, Class<T> Type) {
        return getContainerData(new NamespacedKey(MComesToLife.getPlugin(), tag), Type);
    }

    public <T> T getContainerData(Memory memory, Class<T> Type) {
        return getContainerData(memory.getKey(), Type);
    }

    public <T> T getContainerData(Memory memory) {
        return (T) getContainerData(memory.getKey(), memory.getBukkitClass());
    }

    public void removeContainerData(NamespacedKey tag) {
        this.villager.getPersistentDataContainer()
                .remove(tag);
    }

    public void removeContainerData(String tag) {
        removeContainerData(new NamespacedKey(MComesToLife.getPlugin(), tag));
    }

    public void removeContainerData(Memory memory) {
        this.villager.getPersistentDataContainer()
                .remove(memory.getKey());
    }

    @Getter
    private net.minecraft.world.entity.LivingEntity nMSEntity = null;

    @Nullable
    public net.minecraft.world.entity.LivingEntity generateNMS() {
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
                if (f.get(getBrain().getBody()) != null) {
                    return ((net.minecraft.world.entity.LivingEntity) ((net.minecraft.world.entity.Mob) f
                            .get(getBrain().getBody())));
                }
            }

            return null;
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static net.minecraft.world.entity.LivingEntity getNmsEntity(LivingEntity entity) {
        try {

            Field f = BukkitBrain.getBrain((Mob) entity).getBody().getClass().getDeclaredField("nmsMob");
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
                        (Mob) entity)));
            } else {
                if (f.get(BukkitBrain.getBrain((Mob) entity).getBody()) != null) {
                    return ((net.minecraft.world.entity.LivingEntity) ((net.minecraft.world.entity.Mob) f
                            .get(BukkitBrain.getBrain((Mob) entity).getBody())));
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
