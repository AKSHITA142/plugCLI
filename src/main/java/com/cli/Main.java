package com.cli;

import com.cli.commands.AddCommand;
import com.cli.commands.HelloCommad;

import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void main(String[] args) {

        CommandRegistry registry = new CommandRegistry();   

        // Register commands manually (temporary for MVP)
        registry.register(new HelloCommad());
        registry.register(new AddCommand());

        if (args.length == 0) {
            System.out.println("No command provided");
            return;
        }

        String commandName = args[0];
        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, commandArgs.length);

        if (!registry.hasCommand(commandName)) {
            System.out.println("Unknown command: " + commandName);
            return;
        }

        Plugin command = registry.getCommand(commandName);
        command.execute(commandArgs);
    }
}