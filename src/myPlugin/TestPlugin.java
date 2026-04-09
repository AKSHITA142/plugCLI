package myplugin;

import com.cli.Plugin;
import com.cli.Command;

@Command(value = "test", version = "1.0")

public class TestPlugin implements Plugin {

    @Override
    public String getCommand() {
        return "test";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Test plugin executed!");
    }
}