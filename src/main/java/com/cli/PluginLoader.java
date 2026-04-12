package com.cli;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginLoader {

    // A small helper class to hold a discovered plugin + its metadata together
    private static class DiscoveredPlugin {
        Plugin plugin;
        Command metadata;

        DiscoveredPlugin(Plugin plugin, Command metadata) {
            this.plugin = plugin;
            this.metadata = metadata;
        }
    }

    public void loadPlugins(CommandRegistry registry) {
        File folder = new File("plugins");

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Plugins folder not found");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) return;

        System.out.println("Loading plugins...\n");

        // ===== PHASE 1: Discover all plugins from all JARs =====
        // We collect everything first, without registering anything yet.
        // Key = command name, Value = list of all plugins that want that command name
        Map<String, List<DiscoveredPlugin>> allDiscovered = new HashMap<>();

        for (File file : files) {
            try {
                URL[] urls = { file.toURI().toURL() };
                URLClassLoader classLoader = new URLClassLoader(urls);

                JarFile jarFile = new JarFile(file);
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();

                    if (entry.getName().endsWith(".class")) {

                        String className = entry.getName()
                                .replace("/", ".")
                                .replace(".class", "");

                        Class<?> clazz = classLoader.loadClass(className);

                        if (Plugin.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                            if (clazz.isAnnotationPresent(Command.class)) {

                                Command commandAnnotation = clazz.getAnnotation(Command.class);
                                String commandName = commandAnnotation.value();

                                Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();

                                // Don't register yet! Just collect.
                                allDiscovered
                                    .computeIfAbsent(commandName, k -> new ArrayList<>())
                                    .add(new DiscoveredPlugin(plugin, commandAnnotation));
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

        // ===== PHASE 2: Process all discovered plugins =====
        ConflictResolver resolver = new ConflictResolver();

        for (Map.Entry<String, List<DiscoveredPlugin>> entry : allDiscovered.entrySet()) {
            String commandName = entry.getKey();
            List<DiscoveredPlugin> candidates = entry.getValue();

            if (candidates.size() == 1) {
                // No conflict — only one plugin wants this command name
                DiscoveredPlugin dp = candidates.get(0);
                registry.register(commandName, dp.plugin, dp.metadata);
                System.out.println("  Loaded: " + commandName
                    + " v" + dp.metadata.version()
                    + " by " + dp.metadata.author());

            } else {
                // CONFLICT — multiple plugins want the same command name
                // Extract the plugin and metadata lists for the resolver
                List<Plugin> pluginList = new ArrayList<>();
                List<Command> metaList = new ArrayList<>();

                for (DiscoveredPlugin dp : candidates) {
                    pluginList.add(dp.plugin);
                    metaList.add(dp.metadata);
                }

                // Ask the user to pick
                int chosenIndex = resolver.resolve(commandName, pluginList, metaList);

                // Register ONLY the chosen one
                DiscoveredPlugin winner = candidates.get(chosenIndex);
                registry.register(commandName, winner.plugin, winner.metadata);
            }
        }

        System.out.println("\nAll plugins loaded successfully!\n");
    }
}