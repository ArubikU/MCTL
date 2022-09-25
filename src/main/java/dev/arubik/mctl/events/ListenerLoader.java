package dev.arubik.mctl.events;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.comphenix.protocol.ProtocolLibrary;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.utils.messageUtils;

public class ListenerLoader {
    public static HashMap<String, Listener> listeners = new HashMap<String, Listener>();
    private static MComesToLife plugin = (MComesToLife) MComesToLife.getPlugin();

    public Collection<Class> getClasses(final String pack) throws Exception {
        final StandardJavaFileManager fileManager = ToolProvider.getSystemJavaCompiler().getStandardFileManager(null,
                null, null);
        return StreamSupport
                .stream(fileManager.list(StandardLocation.CLASS_PATH, pack,
                        Collections.singleton(JavaFileObject.Kind.CLASS), false).spliterator(), false)
                .map(javaFileObject -> {
                    try {
                        final String[] split = javaFileObject.getName()
                                .replace(".class", "")
                                .replace(")", "")
                                .split(Pattern.quote(File.separator));

                        final String fullClassName = pack + "." + split[split.length - 1];
                        return Class.forName(fullClassName);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addListener(Listener listener) {
        listeners.put(listener.getClass().getSimpleName(), listener);
    }

    public void loadListener(String listener) {
        try {
            // get a class from dev.arubik.mctl.events.listeners
            Class<?> clazz = Class.forName("dev.arubik.mctl.events.listeners." + listener);
            Listener l = (Listener) clazz.newInstance();
            listeners.put(listener, l);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAllListeners() {
        // get all clases from dev.arubik.mctl.events.listeners
        try {
            Collection<Class> classes = getClasses("dev.arubik.mctl.events.listeners");
            for (Class clazz : classes) {
                Listener l = (Listener) clazz.newInstance();
                listeners.put(clazz.getSimpleName(), l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RegisterListener() {
        for (String listener : listeners.keySet()) {
            listeners.get(listener).register();
            messageUtils.log("Registered listener: " + listener);
        }
    }

    public void removeListener() {
        for (String listener : listeners.keySet()) {
            listeners.get(listener).unregister();
            messageUtils.log("Unregistered listener: " + listener);
        }
    }
}
