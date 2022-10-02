package dev.arubik.mctl.holders;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dev.arubik.mctl.MComesToLife;

public class Nms {
    private static String VERSION = MComesToLife.getServerVersion();

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
}
