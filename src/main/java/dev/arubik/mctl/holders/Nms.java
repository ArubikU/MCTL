package dev.arubik.mctl.holders;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import dev.arubik.mctl.MComesToLife;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class Nms {
    private static String VERSION = MComesToLife.getServerVersion();
    private static HashMap<String, Class<?>> nmsClasses = new HashMap<String, Class<?>>();

    /*
     * @param className the ClassName replacing {version} with version
     * 
     */
    public <T> T castMethod(String className, String methodName, Class<T> BukkitClass, Object... args) {
        Object object = null;
        try {
            Class a = Class.forName(className.replace("{version}", VERSION));

            Method b = null;
            for (Method c : a.getDeclaredMethods()) {
                if (c.getName().equalsIgnoreCase(methodName)) {
                    b = c;
                }
            }
            if (b == null)
                return null;
            b.setAccessible(true);
            object = b.invoke(a, args);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (object == null)
            return null;
        return (T) object;
    }

    public <T> T getObject(String className, String objectName, Class<T> BukkitClass, Object instance) {
        Object object = null;
        try {
            Class a = Class.forName(className.replace("{version}", VERSION));
            Class<?> b = (Class<?>) a.cast(instance).getClass();
            Field c = null;
            for (Field d : b.getDeclaredFields()) {
                if (d.getName().equalsIgnoreCase(objectName)) {
                    c = d;
                }
            }
            if (c == null)
                return null;
            c.setAccessible(true);
            return (T) c.get(instance);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (object == null)
            return null;
        return (T) object;
    }

    public <T> T castMethodAndGetObject(String firstCast, String methodName, Class<T> toCastReturn, Object instance) {
        Object object = null;
        try {
            Class temp = Class.forName(firstCast.replace("{version}", VERSION));
            Class a = temp.cast(instance).getClass();
            Method b = null;
            for (Method c : a.getDeclaredMethods()) {
                if (c.getName().equalsIgnoreCase(methodName)) {
                    b = c;
                }
            }
            if (b == null)
                return null;
            b.setAccessible(true);
            object = b.invoke(a, instance);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (object == null)
            return null;
        return (T) object;
    }

    public void forceMethod(Object instance, String methodName) {
        try {
            Class<?> a = instance.getClass();
            Method b = null;
            for (Method c : a.getDeclaredMethods()) {
                if (c.getName().equalsIgnoreCase(methodName)) {
                    b = c;
                }
            }
            if (b == null)
                return;
            b.setAccessible(true);
            b.invoke(instance);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public net.minecraft.world.item.ItemStack getNMSStackFromStack(ItemStack stack) {
        return castMethod("org.bukkit.craftbukkit.{version}.inventory.CraftItemStack", "asNMSCopy",
                net.minecraft.world.item.ItemStack.class, stack);
    }

    @Nullable
    public Item getNMSItemFromStack(ItemStack stack) {
        net.minecraft.world.item.ItemStack st = getNMSStackFromStack(stack);
        if (st == null)
            return null;
        return st.getItem();
    }

    @Nullable
    public DedicatedServer getServer(){
        return castMethodAndGetObject("org.bukkit.craftbukkit.{version}.CraftServer", "getServer", DedicatedServer.class, Bukkit.getServer());
    }

    @Nullable
    public DedicatedPlayerList getPlayers(){
        return castMethodAndGetObject("org.bukkit.craftbukkit.{version}.CraftServer", "getHandle", DedicatedPlayerList.class, Bukkit.getServer());
    }

    @Nullable
    public ServerPlayer getNMSPlayer(org.bukkit.entity.Player player){
        return castMethodAndGetObject("org.bukkit.craftbukkit.{version}.entity.CraftPlayer", "getHandle", ServerPlayer.class, player);
    }
}
