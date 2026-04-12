package com.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // 1. Initialize components
        CommandRegistry registry = new CommandRegistry();
        PluginLoader loader = new PluginLoader();
        CLIParser parser = new CLIParser();
        ExecutionEngine engine = new ExecutionEngine();

        // 2. Load external plugins (reads saved choices, only asks on first run)
        loader.loadPlugins(registry);

        // 3. Parse input
        String commandName = parser.getCommandName(args);

        if (commandName == null) {
            System.out.println("No command provided");
            return;
        }

        // 4. Handle help command
        if (commandName.equals("help")) {
            registry.printAllCommands();

            // Show switch options if there are any conflicts
            printSwitchHint(loader);
            return;
        }

        // 5. Handle switch command: "switch add" changes the active version of "add"
        if (commandName.equals("switch")) {
            handleSwitch(args, parser, loader, registry);
            return;
        }

        // 6. Find and execute command
        if (!registry.hasCommand(commandName)) {
            System.out.println("Unknown command: " + commandName);
            return;
        }

        String[] commandArgs = parser.getCommandArgs(args);
        Plugin command = registry.getCommand(commandName);
        engine.execute(command, commandArgs);
    }

    /**
     * Prints a hint about the switch command if conflicts exist.
     */
    private static void printSwitchHint(PluginLoader loader) {
        boolean hasConflicts = false;

        for (Map.Entry<String, ?> entry : loader.getAllDiscovered().entrySet()) {
            List<?> candidates = (List<?>) entry.getValue();
            if (candidates.size() > 1) {
                if (!hasConflicts) {
                    System.out.println("┌──────────────────────────────────────────────────┐");
                    System.out.println("│  Some commands have multiple versions available:  │");
                    System.out.println("├──────────────────────────────────────────────────┤");
                    hasConflicts = true;
                }
                System.out.println("│  → To change version, run:  switch " + entry.getKey());
            }
        }

        if (hasConflicts) {
            System.out.println("└──────────────────────────────────────────────────┘");
        }
    }

    /**
     * Handles the "switch" command: lets user change which version of a conflicting
     * command is active.
     *
     * Usage: java -cp out com.cli.Main switch add
     */
    private static void handleSwitch(String[] args, CLIParser parser,
                                      PluginLoader loader, CommandRegistry registry) {
        String[] switchArgs = parser.getCommandArgs(args);

        if (switchArgs.length == 0) {
            System.out.println("Usage: switch <command-name>");
            System.out.println("Example: switch add");
            return;
        }

        String targetCommand = switchArgs[0];
        Map<String, ?> allDiscovered = loader.getAllDiscovered();

        if (!allDiscovered.containsKey(targetCommand)) {
            System.out.println("No command found: " + targetCommand);
            return;
        }

        List<?> candidates = (List<?>) allDiscovered.get(targetCommand);

        if (candidates.size() <= 1) {
            System.out.println("'" + targetCommand + "' has only one version. Nothing to switch.");
            return;
        }

        // Build plugin and meta lists for the resolver
        List<Plugin> pluginList = new ArrayList<>();
        List<Command> metaList = new ArrayList<>();

        for (Object obj : candidates) {
            pluginList.add(PluginLoader.getPlugin(obj));
            metaList.add(PluginLoader.getMetadata(obj));
        }

        // Show the conflict table and ask user to pick
        ConflictResolver resolver = new ConflictResolver();
        int chosenIndex = resolver.resolve(targetCommand, pluginList, metaList);

        // Update the registry with the new choice
        Plugin chosenPlugin = pluginList.get(chosenIndex);
        Command chosenMeta = metaList.get(chosenIndex);

        registry.register(targetCommand, chosenPlugin, chosenMeta);

        // Save the new choice to config file
        loader.getConflictConfig().saveChoice(targetCommand,
            chosenMeta.version(), chosenMeta.author());

        System.out.println("Done! Next time you run '" + targetCommand
            + "', it will use v" + chosenMeta.version() + " by " + chosenMeta.author());
    }
}