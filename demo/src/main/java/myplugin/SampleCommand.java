package myplugin;

import com.cli.Plugin;
import com.cli.Command;
import com.cli.ArgParser;

@Command(
    value = "sample",
    version = "1.0",
    description = "A sample command to get you started",
    author = "Your Name",
    usage = "sample <your-name>"
)
public class SampleCommand implements Plugin {

    @Override
    public String getCommand() {
        return "sample";
    }

    @Override
    public void execute(String[] args) {
        ArgParser parser = new ArgParser(args);
        String name = parser.getString(0, "World");
        System.out.println("Hello, " + name + "!");
    }
}
