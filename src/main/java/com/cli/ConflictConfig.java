package com.cli;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads and writes conflict resolution choices to a config file.
 * This way, the user is only asked ONCE about each conflict.
 *
 * File format (each line):
 *   command-name=version|author
 * Example:
 *   add=2.0|Akshita
 */
public class ConflictConfig {

    private static final String CONFIG_FILE = "plugins/conflict-choices.config";

    // Stores: command-name → "version|author" string
    private Map<String, String> savedChoices = new HashMap<>();

    /**
     * Load saved conflict choices from the config file.
     * If the file does not exist, savedChoices will be empty (first run).
     */
    public void load() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    savedChoices.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("  Warning: Could not read " + CONFIG_FILE);
        }
    }

    /**
     * Check if we have a saved choice for a specific command.
     */
    public boolean hasSavedChoice(String commandName) {
        return savedChoices.containsKey(commandName);
    }

    /**
     * Get the saved choice for a command.
     * Returns a string like "2.0|Akshita" or null if no choice saved.
     */
    public String getSavedChoice(String commandName) {
        return savedChoices.get(commandName);
    }

    /**
     * Save a new conflict choice.
     * This updates the in-memory map AND writes to the file.
     */
    public void saveChoice(String commandName, String version, String author) {
        savedChoices.put(commandName, version + "|" + author);
        writeToFile();
    }

    /**
     * Remove a saved choice (used when user wants to re-choose from help menu).
     */
    public void removeChoice(String commandName) {
        savedChoices.remove(commandName);
        writeToFile();
    }

    /**
     * Write all saved choices to the config file.
     */
    private void writeToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CONFIG_FILE))) {
            writer.println("# PlugCLI Conflict Resolutions (auto-generated)");
            writer.println("# Format: command-name=version|author");
            writer.println("# Delete this file to reset all choices.");
            writer.println();

            for (Map.Entry<String, String> entry : savedChoices.entrySet()) {
                writer.println(entry.getKey() + "=" + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("  Warning: Could not write " + CONFIG_FILE);
        }
    }
}