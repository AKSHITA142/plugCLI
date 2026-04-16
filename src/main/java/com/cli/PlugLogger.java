package com.cli;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlugLogger {

    // ANSI color codes for terminal output
    private static final String RESET  = "\u001B[0m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GRAY   = "\u001B[90m";

    private static final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static boolean debugEnabled = false;

    // Enable or disable debug messages
    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

    // INFO — general information
    public static void info(String message) {
        log(GREEN, "INFO", message);
    }

    // WARN — something might be wrong
    public static void warn(String message) {
        log(YELLOW, "WARN", message);
    }

    // ERROR — something went wrong
    public static void error(String message) {
        log(RED, "ERROR", message);
    }

    // ERROR with exception details
    public static void error(String message, Exception e) {
        log(RED, "ERROR", message + " → " + e.getMessage());
    }

    // DEBUG — only prints if debug mode is on
    public static void debug(String message) {
        if (debugEnabled) {
            log(CYAN, "DEBUG", message);
        }
    }

    // Core log method
    private static void log(String color, String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.printf("%s[%s]%s %s%-5s%s %s%n",
            GRAY, timestamp, RESET,
            color, level, RESET,
            message
        );
    }
}