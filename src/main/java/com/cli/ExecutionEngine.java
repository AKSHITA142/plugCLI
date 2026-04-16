package com.cli;

public class ExecutionEngine {

    private HookRegistry hookRegistry;

    public ExecutionEngine() {
        this.hookRegistry = new HookRegistry();
    }

    public HookRegistry getHookRegistry() {
        return hookRegistry;
    }

    public void execute(Plugin command, String[] args) {
        String commandName = command.getCommand();
        try {
            // Run BEFORE hooks
            hookRegistry.runBeforeHooks(commandName, args);

            // Run the actual command
            command.execute(args);

            // Run AFTER hooks
            hookRegistry.runAfterHooks(commandName, args);

        } catch (Exception e) {
            System.out.println("Error executing: " + commandName);
            System.out.println("Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }
}