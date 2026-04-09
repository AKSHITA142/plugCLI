package com.cli;

public class Main {


    public static void main(String[] args) {

        // 1. Initialize components
        CommandRegistry registry = new CommandRegistry();
        PluginLoader loader = new PluginLoader();
        CLIParser parser = new CLIParser();
        ExecutionEngine engine = new ExecutionEngine();

        // Step 2: Load external plugins
        loader.loadPlugins(registry);

        // 4. Parse input
        String commandName = parser.getCommandName(args);

        if (commandName == null) {
            System.out.println("No command provided");
            return;
        }

        String[] commandArgs = parser.getCommandArgs(args);

        // 5. Find command
        if (!registry.hasCommand(commandName)) {
            System.out.println("Unknown command: " + commandName);
            return;
        }

        Plugin command = registry.getCommand(commandName);

        engine.execute(command, commandArgs);
    }
}