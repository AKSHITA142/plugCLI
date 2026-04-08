package com.cli.commands;

import com.cli.Plugin;

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