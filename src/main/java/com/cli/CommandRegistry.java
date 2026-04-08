package com.cli;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private Map<String, Plugin> commandMap = new HashMap<>();

    public void register(Plugin plugin) {
        commandMap.put(plugin.getCommand(), plugin);
    }

    public Plugin getCommand(String commandName) {
        return commandMap.get(commandName);
    }

    public boolean hasCommand(String commandName) {
        return commandMap.containsKey(commandName);
    }
}