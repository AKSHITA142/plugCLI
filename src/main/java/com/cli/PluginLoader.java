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

    private static class DiscoveredPlugin {
        Plugin plugin;
        Command metadata;

        DiscoveredPlugin(Plugin plugin, Command metadata) {
            this.plugin = plugin;
            this.metadata = metadata;
        }
    }

    // Store ALL discovered plugins (including non-chosen ones) so help can access them
    private Map<String, List<DiscoveredPlugin>> allDiscovered = new HashMap<>();
    private ConflictConfig conflictConfig = new ConflictConfig();

    public void loadPlugins(CommandRegistry registry) {
        File folder = new File("plugins");

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Plugins folder not found");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) return;

        System.out.println("Loading plugins...\n");

        // Load saved conflict choices from file
        conflictConfig.load();

        // ===== PHASE 1: Discover all plugins from all JARs =====
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

        // ===== PHASE 2: Resolve conflicts and register =====
        ConflictResolver resolver = new ConflictResolver();

        for (Map.Entry<String, List<DiscoveredPlugin>> entry : allDiscovered.entrySet()) {
            String commandName = entry.getKey();
            List<DiscoveredPlugin> candidates = entry.getValue();

            if (candidates.size() == 1) {
                // No conflict
                DiscoveredPlugin dp = candidates.get(0);
                registry.register(commandName, dp.plugin, dp.metadata);
                System.out.println("  Loaded: " + commandName
                    + " v" + dp.metadata.version()
                    + " by " + dp.metadata.author());

            } else {
                // CONFLICT — check if we have a saved choice
                if (conflictConfig.hasSavedChoice(commandName)) {
                    // We have a saved choice! Use it silently.
                    String saved = conflictConfig.getSavedChoice(commandName);
                    String[] parts = saved.split("\\|", 2);
                    String savedVersion = parts[0];
                    String savedAuthor = parts.length > 1 ? parts[1] : "";

                    // Find the matching plugin
                    DiscoveredPlugin match = null;
                    for (DiscoveredPlugin dp : candidates) {
                        if (dp.metadata.version().equals(savedVersion)
                                && dp.metadata.author().equals(savedAuthor)) {
                            match = dp;
                            break;
                        }
                    }

                    if (match != null) {
                        // Found the saved choice — register silently
                        registry.register(commandName, match.plugin, match.metadata);
                        System.out.println("  Loaded: " + commandName
                            + " v" + match.metadata.version()
                            + " by " + match.metadata.author()
                            + " (saved preference)");
                    } else {
                        // Saved choice no longer exists (plugin was removed/updated)
                        // Ask the user again
                        System.out.println("  Saved choice for '" + commandName
                            + "' is no longer valid. Please re-select:");
                        int chosenIndex = askUser(resolver, commandName, candidates);
                        DiscoveredPlugin winner = candidates.get(chosenIndex);
                        registry.register(commandName, winner.plugin, winner.metadata);
                    }

                } else {
                    // No saved choice — ask user for the first time
                    int chosenIndex = askUser(resolver, commandName, candidates);
                    DiscoveredPlugin winner = candidates.get(chosenIndex);
                    registry.register(commandName, winner.plugin, winner.metadata);
                }
            }
        }

        System.out.println("\nAll plugins loaded successfully!\n");
    }

    /**
     * Ask the user to pick a version and save their choice.
     */
    private int askUser(ConflictResolver resolver, String commandName,
                        List<DiscoveredPlugin> candidates) {
        List<Plugin> pluginList = new ArrayList<>();
        List<Command> metaList = new ArrayList<>();

        for (DiscoveredPlugin dp : candidates) {
            pluginList.add(dp.plugin);
            metaList.add(dp.metadata);
        }

        int chosenIndex = resolver.resolve(commandName, pluginList, metaList);

        // Save the choice so we never ask again
        DiscoveredPlugin winner = candidates.get(chosenIndex);
        conflictConfig.saveChoice(commandName,
            winner.metadata.version(), winner.metadata.author());

        return chosenIndex;
    }

    /**
     * Get all discovered plugins for a command (used by help menu to show switch options).
     */
    public Map<String, List<DiscoveredPlugin>> getAllDiscovered() {
        return allDiscovered;
    }

    /**
     * Get the conflict config (used by help menu to update choices).
     */
    public ConflictConfig getConflictConfig() {
        return conflictConfig;
    }

    // Make DiscoveredPlugin accessible to other classes
    public static Plugin getPlugin(Object dp) {
        return ((DiscoveredPlugin) dp).plugin;
    }

    public static Command getMetadata(Object dp) {
        return ((DiscoveredPlugin) dp).metadata;
    }
}