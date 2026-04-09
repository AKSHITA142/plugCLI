package com.cli;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private Map<String, Plugin> commandMap = new HashMap<>();

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