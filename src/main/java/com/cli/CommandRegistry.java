package com.cli;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    // Stores command-name → Plugin object (for execution)
    private Map<String, Plugin> commandMap = new HashMap<>();

    // Stores command-name → its @Command annotation (for metadata like description, author)
    private Map<String, Command> metadataMap = new HashMap<>();

    public void register(String commandName, Plugin plugin, Command metadata) {
        commandMap.put(commandName, plugin);
        metadataMap.put(commandName, metadata);
    }

    // Overloaded version for backward compatibility (no metadata)
    public void register(String commandName, Plugin plugin) {
        commandMap.put(commandName, plugin);
    }

    public Plugin getCommand(String commandName) {
        return commandMap.get(commandName);
    }

    public boolean hasCommand(String commandName) {
        return commandMap.containsKey(commandName);
    }

    public void printAllCommands() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     AVAILABLE COMMANDS                      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");

        for (Map.Entry<String, Plugin> entry : commandMap.entrySet()) {
            String name = entry.getKey();
            Command meta = metadataMap.get(name);

            System.out.println("║");
            System.out.printf("║  Command:     %s%n", name);

            if (meta != null) {
                System.out.printf("║  Description: %s%n", meta.description());
                System.out.printf("║  Version:     %s%n", meta.version());
                System.out.printf("║  Author:      %s%n", meta.author());
                if (!meta.usage().isEmpty()) {
                    System.out.printf("║  Usage:       %s%n", meta.usage());
                }
            }

            System.out.println("║──────────────────────────────────────────────────────────────");
        }

        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }
}