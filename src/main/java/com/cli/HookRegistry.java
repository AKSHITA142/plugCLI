package com.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HookRegistry {

    // "before" hooks: run BEFORE a command executes
    private Map<String, List<Consumer<String[]>>> beforeHooks = new HashMap<>();

    // "after" hooks: run AFTER a command executes
    private Map<String, List<Consumer<String[]>>> afterHooks = new HashMap<>();

    // Global hooks: run before/after EVERY command
    private List<Consumer<String[]>> globalBeforeHooks = new ArrayList<>();
    private List<Consumer<String[]>> globalAfterHooks = new ArrayList<>();

    // Register a before-hook for a specific command
    public void addBeforeHook(String commandName, Consumer<String[]> hook) {
        beforeHooks.computeIfAbsent(commandName, k -> new ArrayList<>()).add(hook);
    }

    // Register an after-hook for a specific command
    public void addAfterHook(String commandName, Consumer<String[]> hook) {
        afterHooks.computeIfAbsent(commandName, k -> new ArrayList<>()).add(hook);
    }

    // Register a global before-hook (runs before ANY command)
    public void addGlobalBeforeHook(Consumer<String[]> hook) {
        globalBeforeHooks.add(hook);
    }

    // Register a global after-hook (runs after ANY command)
    public void addGlobalAfterHook(Consumer<String[]> hook) {
        globalAfterHooks.add(hook);
    }

    // Run all "before" hooks for a given command
    public void runBeforeHooks(String commandName, String[] args) {
        // Run global hooks first
        for (Consumer<String[]> hook : globalBeforeHooks) {
            hook.accept(args);
        }
        // Run command-specific hooks
        List<Consumer<String[]>> hooks = beforeHooks.get(commandName);
        if (hooks != null) {
            for (Consumer<String[]> hook : hooks) {
                hook.accept(args);
            }
        }
    }

    // Run all "after" hooks for a given command
    public void runAfterHooks(String commandName, String[] args) {
        // Run command-specific hooks first
        List<Consumer<String[]>> hooks = afterHooks.get(commandName);
        if (hooks != null) {
            for (Consumer<String[]> hook : hooks) {
                hook.accept(args);
            }
        }
        // Run global hooks
        for (Consumer<String[]> hook : globalAfterHooks) {
            hook.accept(args);
        }
    }
}