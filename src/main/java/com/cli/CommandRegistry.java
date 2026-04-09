package com.cli;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private Map<String, Plugin> commandMap = new HashMap<>();

    public void printAllCommands() {
        System.out.println("Available commands:");

        for (String cmd : commandMap.keySet()) {
            System.out.println("- " + cmd);
        }
    }

    public void register(String commandName, Plugin plugin) {
      commandMap.put(commandName, plugin);
    }

    public Plugin getCommand(String commandName) {
        return commandMap.get(commandName);
    }

    public boolean hasCommand(String commandName) {
        return commandMap.containsKey(commandName);
    }
}