package com.cli;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginLoader {

    public void loadPlugins(CommandRegistry registry) {
        File folder = new File("plugins");

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Plugins folder not found");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));

        if (files == null) return;

        for (File file : files) {
            try {
                URL[] urls = { file.toURI().toURL() };
                URLClassLoader classLoader = new URLClassLoader(urls);

                // TODO: Load classes from jar (we’ll improve later)
                System.out.println("Loaded plugin: " + file.getName());

            } catch (Exception e) {
                System.out.println("Failed to load plugin: " + file.getName());
                e.printStackTrace();
            }
        }
    }
}