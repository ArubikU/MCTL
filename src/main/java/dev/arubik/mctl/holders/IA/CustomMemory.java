package dev.arubik.mctl.holders.IA;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import me.gamercoder215.mobchip.ai.memories.Memory;

public final class CustomMemory<T> implements Memory<T> {

    enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK
    }

    /**
     * Whether this Entity is not admiring another entity.
     */
    public static final CustomMemory<Boolean> SHIELDING = new CustomMemory<>(Boolean.class, "shield_using");
    public static final CustomMemory<Boolean> NEXT_SHIELDING = new CustomMemory<>(Boolean.class, "next_shield_using");
    public static final CustomMemory<String> CROSSBOW = new CustomMemory<>(String.class, "cross_bow");


    private final Class<T> bukkit;

    private final String key;

    private boolean inverse = false;

    private CustomMemory(Class<T> bukkit, String key) {
        this.bukkit = bukkit;
        this.key = key;
    }

    private CustomMemory(Class<T> clazz, String key, boolean inverse) {
        this(clazz, key);
        this.inverse = inverse;
    }

    /**
     * Get the Bukkit Class of this EntityMemory.
     * 
     * @return Bukkit Class
     */
    @Override
    public Class<T> getBukkitClass() {
        return this.bukkit;
    }

    /**
     * Fetches the NamespacedKey for this Memory.
     * 
     * @return NamespacedKey
     */
    @NotNull
    @Override
    public NamespacedKey getKey() {
        return NamespacedKey.minecraft(this.key);
    }

}
