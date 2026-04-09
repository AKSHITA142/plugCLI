package com.cli;

public class ExecutionEngine {

    public void execute(Plugin command, String[] args) {
        try {
            System.out.println("Executing command: " + command.getCommand());

            command.execute(args);

            System.out.println("Execution completed");

        } catch (Exception e) {
            System.out.println("Error while executing command: " + command.getCommand());
            System.out.println("Reason: " + e.getMessage());

            // Optional: print full stack trace
            e.printStackTrace();
        }
    }
}