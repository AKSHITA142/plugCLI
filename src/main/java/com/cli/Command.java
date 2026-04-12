package com.cli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value();  // command name (required)
    String version() default "1.0"; // version number
    String description() default "No description provided";  // what does this command do
    String author() default "Unknown"; // who wrote this plugin
    String usage() default ""; // usage example like "add <num1> <num2>"
}