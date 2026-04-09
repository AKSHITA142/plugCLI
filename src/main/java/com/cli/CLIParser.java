package com.cli;

public class CLIParser {

    public String getCommandName(String[] args) {
        if (args.length == 0) return null;
        return args[0];
    }

    public String[] getCommandArgs(String[] args) {
        if (args.length <= 1) return new String[0];

        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, commandArgs.length);
        return commandArgs;
    }
}
