package myplugin;

import com.cli.Plugin;
import com.cli.Command;

@Command("hello")

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