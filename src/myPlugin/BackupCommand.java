package myplugin;

import com.cli.Plugin;
import com.cli.Command;
import com.cli.PlugLogger;

@Command(value = "backup", version = "1.0", description = "Backs up data", author = "Akshita")

/*
 * === OOP CONCEPTS USED ===
 * 1. INHERITANCE → "implements Plugin" — BackupCommand inherits the contract from the Plugin interface and provides concrete behavior.
 * 2. POLYMORPHISM → execute() provides BackupCommand's own implementation — when called via Plugin reference, this specific logic runs (runtime polymorphism).
 * 3. ABSTRACTION → Uses PlugLogger for logging (doesn't deal with formatting/colors directly) and @Command annotation for metadata.
 */
public class BackupCommand implements Plugin {

    @Override
    public String getCommand() {
        return "backup";
    }

    @Override
    public void execute(String[] args) {
        PlugLogger.info("Starting backup...");
        PlugLogger.debug("Reading from source directory");
        // ... do work ...
        PlugLogger.info("Backup completed successfully!");
    }
}
