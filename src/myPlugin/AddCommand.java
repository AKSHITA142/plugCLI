package myplugin;

import com.cli.Plugin;
import com.cli.Command;
import com.cli.ArgParser;

@Command(
    value = "add",
    version = "2.0",
    description = "Adds two integers together and prints the result",
    author = "Akshita",
    usage = "add <number1> <number2> [--verbose]"
)
public class AddCommand implements Plugin {

    @Override
    public String getCommand() {
        return "add";
    }

    @Override
    public void execute(String[] args) {
        ArgParser parser = new ArgParser(args);

        if (parser.size() < 2) {
            System.out.println("Usage: add <number1> <number2>");
            return;
        }

        int a = parser.getInt(0);
        int b = parser.getInt(1);
        boolean verbose = parser.hasFlag("--verbose");

        if (verbose) {
            System.out.println("Adding " + a + " + " + b + "...");
        }

        System.out.println("Result: " + (a + b));
    }
}