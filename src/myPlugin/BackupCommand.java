package myplugin;

import com.cli.Plugin;
import com.cli.Command;
import com.cli.PlugLogger;

@Command(value = "backup", version = "1.0", description = "Backs up data", author = "Akshita")

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
