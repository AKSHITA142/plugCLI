package com.cli;

public interface Plugin {
  String getCommand();
  void execute(String[] args);
}