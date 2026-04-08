package com.cli;

import com.cli.commands.AddCommand;
import com.cli.commands.HelloCommad;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<String, Plugin> commands = new HashMap<>();

    public static void main(String[] args) {

        // Register commands manually (temporary for MVP)
        register(new HelloCommad());
        register(new AddCommand());

        if (args.length == 0) {
            System.out.println("No command provided");
            return;
        }

        String commandName = args[0];
        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, commandArgs.length);

        Plugin command = commands.get(commandName);

        if (command == null) {
            System.out.println("Unknown command: " + commandName);
            return;
        }

        command.execute(commandArgs);
    }

    private static void register(Plugin plugin) {
        commands.put(plugin.getCommand(), plugin);
    }
}