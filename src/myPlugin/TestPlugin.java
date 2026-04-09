package myplugin;

import com.cli.Plugin;

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