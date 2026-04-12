package com.cli;

import java.util.List;
import java.util.Scanner;

public class ConflictResolver {

    private Scanner scanner;

    public ConflictResolver() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Called when multiple plugins try to register the same command name.
     * Shows the user all conflicting versions and asks them to pick one.
     *
     * @param commandName  The command name that has conflicts (e.g. "add")
     * @param plugins      List of all Plugin objects that want this command name
     * @param metadatas    List of all @Command annotations for those plugins
     * @return             The index (0-based) of the user's chosen plugin
     */
    public int resolve(String commandName, List<Plugin> plugins, List<Command> metadatas) {

        System.out.println();
        System.out.println("  ⚠  CONFLICT DETECTED for command: \"" + commandName + "\"");
        System.out.println("     Multiple plugins want to register the same command name.");
        System.out.println("     Please choose which version to use:");
        System.out.println();

        // Print the table header
        System.out.println("     ┌───┬─────────┬────────────┬─────────────────────────────────────────┐");
        System.out.println("     │ # │ Version │ Author     │ Description                             │");
        System.out.println("     ├───┼─────────┼────────────┼─────────────────────────────────────────┤");

        // Print each option
        for (int i = 0; i < plugins.size(); i++) {
            Command meta = metadatas.get(i);

            String version = padRight(meta.version(), 7);
            String author  = padRight(meta.author(), 10);
            String desc    = padRight(truncate(meta.description(), 37), 37);

            System.out.printf("     │ %d │ %s │ %s │ %s │%n",
                (i + 1), version, author, desc);
        }

        System.out.println("     └───┴─────────┴────────────┴─────────────────────────────────────────┘");
        System.out.println();

        // Ask the user to choose
        int choice = -1;
        while (choice < 1 || choice > plugins.size()) {
            System.out.print("     Enter your choice (1-" + plugins.size() + "): ");

            String input = scanner.nextLine().trim();

            try {
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > plugins.size()) {
                    System.out.println("     Please enter a number between 1 and " + plugins.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("     Invalid input. Please enter a number.");
            }
        }

        // Print confirmation
        Command chosen = metadatas.get(choice - 1);
        System.out.println();
        System.out.println("     ✅ Using \"" + commandName + "\" v" + chosen.version()
            + " by " + chosen.author());
        System.out.println();

        return choice - 1;  // Convert to 0-based index
    }

    // Helper: pad a string to a fixed width (for table alignment)
    private String padRight(String text, int width) {
        if (text.length() >= width) return text.substring(0, width);
        return String.format("%-" + width + "s", text);
    }

    // Helper: truncate long strings and add "..." at the end
    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}