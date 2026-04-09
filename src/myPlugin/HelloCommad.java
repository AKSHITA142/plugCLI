package myplugin;

import com.cli.Plugin;
import com.cli.Command;

@Command(value = "hello", version = "1.0")

public class HelloCommad implements Plugin {
  @Override
  public String getCommand() {
    return "hello";
  }

  @Override
  public void execute(String[] args) {
    System.out.println("Hello, World!");
  }
}