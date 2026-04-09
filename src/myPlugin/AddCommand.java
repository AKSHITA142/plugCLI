package myplugin;

import com.cli.Plugin;
import com.cli.Command;

@Command(value = "add", version = "1.0")

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