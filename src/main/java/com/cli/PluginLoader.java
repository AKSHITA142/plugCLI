package com.cli;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Enumeration;

public class PluginLoader {

    public void loadPlugins(CommandRegistry registry) {
        File folder = new File("plugins");

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Plugins folder not found");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) return;

        for (File file : files) {
            try {
                URL[] urls = { file.toURI().toURL() };
                URLClassLoader classLoader = new URLClassLoader(urls);

                JarFile jarFile = new JarFile(file);
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();

                    // Only process .class files
                    if (entry.getName().endsWith(".class")) {

                        String className = entry.getName()
                                .replace("/", ".")
                                .replace(".class", "");

                        Class<?> clazz = classLoader.loadClass(className);

                        // Check if class implements Plugin
                        if (Plugin.class.isAssignableFrom(clazz) && !clazz.isInterface()) {

                          if (clazz.isAnnotationPresent(Command.class)) {

                              Command commandAnnotation = clazz.getAnnotation(Command.class);
                              String commandName = commandAnnotation.value();

                              Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();

                              registry.register(commandName, plugin);

                              System.out.println("Registered command (annotation): " + commandName);
                          }
                      }
                    }
                }

                jarFile.close();

            } catch (Exception e) {
                System.out.println("Failed to load plugin: " + file.getName());
                e.printStackTrace();
            }
        }
    }
}