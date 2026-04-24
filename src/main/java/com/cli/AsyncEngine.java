package com.cli;

import java.util.Map;
import java.util.concurrent.*;

/*
 * === OOP CONCEPTS USED ===
 * 1. ENCAPSULATION → ExecutorService and runningTasks map are private; controlled via public methods (executeAsync, cancelTask, printStatus).
 * 2. POLYMORPHISM  → command.execute(args) calls the specific plugin's implementation at runtime — same method call, different behavior (runtime polymorphism).
 * 3. COMPOSITION   → AsyncEngine "HAS-A" ExecutorService and a Map of Futures (uses them internally, doesn't inherit from them).
 * 4. CONCURRENCY   → Uses ExecutorService and Future from java.util.concurrent — runs commands in separate threads (multithreading concept).
 */
public class AsyncEngine {

    private ExecutorService executor = Executors.newCachedThreadPool();
    private Map<String, Future<?>> runningTasks = new ConcurrentHashMap<>();

    public void executeAsync(Plugin command, String[] args) {
        String commandName = command.getCommand();

        Future<?> future = executor.submit(() -> {
            try {
                PlugLogger.info("[ASYNC] Started: " + commandName);
                command.execute(args);
                PlugLogger.info("[ASYNC] Completed: " + commandName);
            } catch (Exception e) {
                PlugLogger.error("[ASYNC] Failed: " + commandName, e);
            } finally {
                runningTasks.remove(commandName);
            }
        });

        runningTasks.put(commandName, future);
        PlugLogger.info("Command '" + commandName + "' is running in background.");
    }

    public void cancelTask(String commandName) {
        Future<?> future = runningTasks.get(commandName);
        if (future != null) {
            future.cancel(true);
            runningTasks.remove(commandName);
            PlugLogger.info("Cancelled: " + commandName);
        } else {
            PlugLogger.warn("No running task found: " + commandName);
        }
    }

    public void printStatus() {
        if (runningTasks.isEmpty()) {
            System.out.println("No background tasks running.");
            return;
        }
        System.out.println("Running background tasks:");
        for (Map.Entry<String, Future<?>> entry : runningTasks.entrySet()) {
            String status = entry.getValue().isDone() ? "DONE" : "RUNNING";
            System.out.println("  - " + entry.getKey() + " [" + status + "]");
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}