package myplugin;

import com.cli.Plugin;
import com.cli.Command;

@Command(
    value = "add",
    version = "1.0",
    description = "Adds two integers together and prints the result",
    author = "Akshita",
    usage = "add <number1> <number2>"
)

public class AddCommand implements Plugin {

    @Override
    public String getCommand() {
        return "add";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide two numbers");
            return;
        }

        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);

        System.out.println("Result: " + (a + b));
    }
}