package com.cli;
public class ArgParser {

    private String[] args;

    public ArgParser(String[] args) {
        this.args = args;
    }

    // Get argument at position as String
    // Example: args = ["5", "10"]  →  getString(0) returns "5"
    public String getString(int index) {
        if (index < 0 || index >= args.length) {
            return null;
        }
        return args[index];
    }

    // Get argument at position as String, with a default fallback
    public String getString(int index, String defaultValue) {
        String val = getString(index);
        return val != null ? val : defaultValue;
    }

    // Get argument at position as int
    // Example: args = ["5", "10"]  →  getInt(0) returns 5
    public int getInt(int index) {
        String val = getString(index);
        if (val == null) {
            throw new IllegalArgumentException("Missing argument at position " + index);
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expected integer at position " + index + ", got: " + val);
        }
    }

    // Get argument at position as int, with default fallback
    public int getInt(int index, int defaultValue) {
        String val = getString(index);
        if (val == null) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Get argument at position as double
    public double getDouble(int index) {
        String val = getString(index);
        if (val == null) {
            throw new IllegalArgumentException("Missing argument at position " + index);
        }
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expected number at position " + index + ", got: " + val);
        }
    }

    // Get argument at position as boolean
    // "true" → true, anything else → false
    public boolean getBoolean(int index) {
        String val = getString(index);
        return "true".equalsIgnoreCase(val);
    }

    // Check if a flag exists: --verbose, --debug, --help
    // Example: args = ["5", "--verbose"]  →  hasFlag("--verbose") returns true
    public boolean hasFlag(String flag) {
        for (String arg : args) {
            if (arg.equals(flag)) return true;
        }
        return false;
    }

    // Get named argument value: --name=Akshita
    // Example: args = ["--name=Akshita"]  →  getNamed("--name") returns "Akshita"
    public String getNamed(String key) {
        for (String arg : args) {
            if (arg.startsWith(key + "=")) {
                return arg.substring(key.length() + 1);
            }
        }
        return null;
    }

    // Get named argument with default fallback
    public String getNamed(String key, String defaultValue) {
        String val = getNamed(key);
        return val != null ? val : defaultValue;
    }

    // Get the total number of arguments
    public int size() {
        return args.length;
    }
}