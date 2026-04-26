package com.cli;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class InitCommand {

    private static final String FRAMEWORK_JAR = "plugcli-core-1.2.1.jar";

    public void execute(String projectName) {
        System.out.println("Scaffolding new PlugCLI plugin project: " + projectName);

        // Create directory structure
        new File(projectName + "/src/main/java/myplugin").mkdirs();
        new File(projectName + "/plugins").mkdirs();

        // Copy the framework JAR into the new project
        copyFrameworkJar(projectName);

        // Generate pom.xml
        writeFile(projectName + "/pom.xml", generatePom(projectName));

        // Generate sample command
        writeFile(projectName + "/src/main/java/myplugin/SampleCommand.java",
                generateSampleCommand());

        // Generate README
        writeFile(projectName + "/README.md", generateReadme(projectName));

        // Generate build script
        writeFile(projectName + "/build-plugin.ps1", generateBuildScript());

        System.out.println("\n✅ Project '" + projectName + "' created successfully!");
        System.out.println("\nNext steps:");
        System.out.println("  1. cd " + projectName);
        System.out.println("  2. Edit src/main/java/myplugin/SampleCommand.java");
        System.out.println("  3. Run: .\\build-plugin.ps1");
        System.out.println("  4. Run: plugcli sample YourName");
    }

    /**
     * Copies the framework JAR into the new project folder so
     * the project is completely self-contained.
     */
    private void copyFrameworkJar(String projectName) {
        List<String> possiblePaths = new ArrayList<>();

        // 1. Global install location (Windows/Linux/Mac)
        String userHome = System.getProperty("user.home");
        possiblePaths.add(userHome + "/.plugcli/plugcli.jar");

        // 2. Development locations
        possiblePaths.add("target/" + FRAMEWORK_JAR);
        possiblePaths.add(FRAMEWORK_JAR);

        // 3. Executing JAR location
        try {
            String executingJar = InitCommand.class.getProtectionDomain().getCodeSource().getLocation().toURI()
                    .getPath();
            if (executingJar.endsWith(".jar")) {
                possiblePaths.add(executingJar);
            }
        } catch (Exception e) {
            // Ignore
        }

        for (String path : possiblePaths) {
            File source = new File(path);
            if (source.exists()) {
                try {
                    Path dest = Path.of(projectName, FRAMEWORK_JAR);
                    Files.copy(source.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("  Copied: " + FRAMEWORK_JAR + " (framework)");
                    return;
                } catch (IOException e) {
                    System.out.println("  Warning: Could not copy from " + path);
                }
            }
        }

        // If JAR not found
        System.out.println("  Warning: Could not find framework JAR to copy.");
        System.out.println("  Note: Please ensure PlugCLI is installed correctly or run 'mvn clean package'.");
    }

    private String generatePom(String projectName) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
                + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + "  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n"
                + "  http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
                + "  <modelVersion>4.0.0</modelVersion>\n"
                + "  <groupId>com.myplugin</groupId>\n"
                + "  <artifactId>" + projectName + "</artifactId>\n"
                + "  <version>1.0.0</version>\n"
                + "  <dependencies>\n"
                + "    <dependency>\n"
                + "      <groupId>io.github.akshita142</groupId>\n"
                + "      <artifactId>plugcli-core</artifactId>\n"
                + "      <version>1.0.0</version>\n"
                + "    </dependency>\n"
                + "  </dependencies>\n"
                + "  <build>\n"
                + "    <plugins>\n"
                + "      <plugin>\n"
                + "        <groupId>org.apache.maven.plugins</groupId>\n"
                + "        <artifactId>maven-jar-plugin</artifactId>\n"
                + "        <version>3.3.0</version>\n"
                + "        <configuration>\n"
                + "          <outputDirectory>${project.basedir}/plugins</outputDirectory>\n"
                + "        </configuration>\n"
                + "      </plugin>\n"
                + "    </plugins>\n"
                + "  </build>\n"
                + "</project>\n";
    }

    private String generateSampleCommand() {
        return "package myplugin;\n\n"
                + "import com.cli.Plugin;\n"
                + "import com.cli.Command;\n"
                + "import com.cli.ArgParser;\n\n"
                + "@Command(\n"
                + "    value = \"sample\",\n"
                + "    version = \"1.0\",\n"
                + "    description = \"A sample command to get you started\",\n"
                + "    author = \"Your Name\",\n"
                + "    usage = \"sample <your-name>\"\n"
                + ")\n"
                + "public class SampleCommand implements Plugin {\n\n"
                + "    @Override\n"
                + "    public String getCommand() {\n"
                + "        return \"sample\";\n"
                + "    }\n\n"
                + "    @Override\n"
                + "    public void execute(String[] args) {\n"
                + "        ArgParser parser = new ArgParser(args);\n"
                + "        String name = parser.getString(0, \"World\");\n"
                + "        System.out.println(\"Hello, \" + name + \"!\");\n"
                + "    }\n"
                + "}\n";
    }

    private String generateReadme(String projectName) {
        return "# " + projectName + "\n\n"
                + "A plugin built with the PlugCLI framework.\n\n"
                + "## Setup\n"
                + "Everything is ready! This project is self-contained.\n\n"
                + "## Build your plugin\n"
                + "```powershell\n"
                + ".\\build-plugin.ps1\n"
                + "```\n\n"
                + "## Run\n"
                + "```powershell\n"
                + "plugcli sample YourName\n"
                + "```\n\n"
                + "## Available commands\n"
                + "```powershell\n"
                + "plugcli help\n"
                + "```\n";
    }

    private String generateBuildScript() {
        return "# PlugCLI Plugin Build Script\n"
                + "# Compiles your plugin and creates a JAR in the plugins/ folder.\n\n"
                + "if (Test-Path \"out_plugin\") { Remove-Item -Recurse -Force \"out_plugin\" }\n"
                + "mkdir \"out_plugin\" -Force | Out-Null\n\n"
                + "# Use the local framework JAR as classpath\n"
                + "$frameworkJar = \"" + FRAMEWORK_JAR + "\"\n"
                + "if (-Not (Test-Path $frameworkJar)) {\n"
                + "    Write-Host \"Framework JAR missing. Copying from global installation...\" -ForegroundColor Yellow\n"
                + "    $globalJar = \"$env:USERPROFILE\\.plugcli\\plugcli.jar\"\n"
                + "    if (Test-Path $globalJar) {\n"
                + "        Copy-Item $globalJar .\\$frameworkJar -Force\n"
                + "        Write-Host \"Copied successfully.\" -ForegroundColor Green\n"
                + "    } else {\n"
                + "        Write-Host \"ERROR: $frameworkJar not found globally either!\" -ForegroundColor Red\n"
                + "        exit 1\n"
                + "    }\n"
                + "}\n\n"
                + "Write-Host \"Compiling plugin code...\"\n"
                + "$javaFiles = Get-ChildItem -Recurse -Filter *.java src | "
                + "Select-Object -ExpandProperty FullName\n\n"
                + "if ($javaFiles) {\n"
                + "    javac -cp $frameworkJar -d out_plugin $javaFiles\n"
                + "    if ($LASTEXITCODE -ne 0) {\n"
                + "        Write-Host \"`nCompilation FAILED!\" -ForegroundColor Red\n"
                + "        exit 1\n"
                + "    }\n"
                + "    Write-Host \"Creating JAR file...\"\n"
                + "    if (-Not (Test-Path \"plugins\")) { mkdir \"plugins\" -Force | Out-Null }\n"
                + "    & \"C:\\Program Files\\Java\\jdk-21\\bin\\jar.exe\" cvf "
                + "plugins/myplugin.jar -C out_plugin .\n"
                + "    Write-Host \"`nBuild successful!\" -ForegroundColor Green\n"
                + "    Write-Host \"Run: plugcli sample YourName\" "
                + "-ForegroundColor Cyan\n"
                + "} else {\n"
                + "    Write-Host \"No Java files found in src/\" -ForegroundColor Red\n"
                + "}\n";
    }

    private void writeFile(String path, String content) {
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
            System.out.println("  Created: " + path);
        } catch (IOException e) {
            System.out.println("  Failed to create: " + path);
        }
    }
}