package com.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {

    private Map<String, String> aliases = new HashMap<>();

    public void load() {
        File configFile = new File("plugcli.config");

        // If the file doesn't exist, we just silently return (no aliases)
        if (!configFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip empty lines and comments (lines starting with #)
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Split "b = backup" into ["b ", " backup"]
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String alias = parts[0].trim();
                    String target = parts[1].trim();
                    aliases.put(alias, target);
                }
            }
        } catch (Exception e) {
            PlugLogger.warn("Could not load plugcli.config: " + e.getMessage());
        }
    }

    // Resolve an alias: "b" → "backup"
    // If no alias exists, it returns the original name unchanged
    public String resolveAlias(String commandName) {
        return aliases.getOrDefault(commandName, commandName);
    }

    public Map<String, String> getAllAliases() {
        return aliases;
    }
}
