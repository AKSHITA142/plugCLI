<h1 align="center">PlugCLI 🚀</h1>

<p align="center">
  <em>A lightweight, extensible, and dynamic Command Line Interface (CLI) framework for Java.</em>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21+-orange.svg" alt="Java 21" />
  <img src="https://img.shields.io/badge/Maven-Central-blue.svg" alt="Maven Central" />
  <img src="https://img.shields.io/badge/License-MIT-green.svg" alt="MIT License" />
</p>

---

## 👋 Welcome to PlugCLI!

**PlugCLI** is a developer-friendly framework that makes building Java CLI applications incredibly easy. Instead of writing one massive file for all your commands, PlugCLI allows you to write isolated **Plugins**. These plugins are compiled into `.jar` files and loaded dynamically at runtime, keeping your project organized, modular, and easy to maintain.

Whether you're building a simple automation script or a complex developer tool, PlugCLI has you covered!

---

## ✨ Features

- **🧩 Dynamic Plugin Loading:** Drop a new plugin JAR into the `plugins/` folder and PlugCLI instantly recognizes it.
- **⚡ Quick Scaffolding:** Use `plugcli init <name>` to generate a ready-to-code plugin template in seconds.
- **🪝 Middleware Hooks:** Run custom code *before* and *after* commands execute (perfect for logging, authentication, or setup).
- **🛠️ Annotation-Based:** Simply use `@Command` to define your CLI commands. No complex routing required!

---

## 🚀 Quick Start (For End Users)

Just want to use the framework on your Windows machine? Let's get it installed!

1. Open your **PowerShell** terminal.
2. Run the following installation command:

```powershell
iwr -useb https://raw.githubusercontent.com/AKSHITA142/plugCLI/main/install.ps1 | iex
```

3. **Restart your terminal.**
4. Verify the installation by typing:

```powershell
plugcli help
```

---

## 🛠️ Creating Your First Plugin (For Developers)

Ready to write your own commands? PlugCLI makes it fast and easy.

### Option 1: The Fast Way (Scaffolding)

If you have PlugCLI installed globally, you can generate a new project structure instantly.

1. Navigate to an empty folder in your terminal.
2. Run the initialization command:

```powershell
plugcli init my-first-plugin
```
*(You can also use `plugcli init .` to initialize in the current directory!)*

3. This will generate a complete project structure, including a sample `HelloCommand` and a `build-plugin.ps1` script.
4. Modify the generated Java file, then run `./build-plugin.ps1` to compile it. Your new command is ready!

### Option 2: The Maven Way (Manual Setup)

If you are integrating PlugCLI into an existing project or prefer manual Maven setup, simply add it as a dependency in your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.akshita142</groupId>
        <artifactId>plugcli-core</artifactId>
        <version>1.2.3</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

**Writing the Code:**
Create a Java class, implement the `Plugin` interface, and add the `@Command` annotation!

```java
import com.cli.Plugin;
import com.cli.Command;
import com.cli.PlugLogger;

@Command(value = "hello", description = "Prints a friendly greeting")
public class HelloCommand implements Plugin {
    @Override
    public void execute(String[] args) {
        PlugLogger.info("Hello from my custom PlugCLI plugin!");
    }
}
```

**Automate the Build:**
Add this to your `pom.xml` to automatically place compiled JARs in the `plugins/` directory:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.3.0</version>
            <configuration>
                <outputDirectory>${project.basedir}/plugins</outputDirectory>
            </configuration>
        </plugin>
    </plugins>
</build>
```
Compile using `mvn clean package`, and your command is instantly available!

---

## 🪝 Advanced: Using Hooks

PlugCLI supports **Hooks**, which act like middleware. You can define logic that runs globally before or after commands are executed.

```java
import com.cli.hooks.Hook;
import com.cli.PlugLogger;

public class MyLoggerHook implements Hook {
    @Override
    public void beforeExecution(String commandName, String[] args) {
        PlugLogger.info("Starting command: " + commandName);
    }

    @Override
    public void afterExecution(String commandName, boolean success) {
        PlugLogger.info("Command finished successfully? " + success);
    }
}
```

---

## 🤝 Contributing

Contributions are always welcome! Feel free to open an issue or submit a pull request if you have ideas on how to make PlugCLI even better.

## 📄 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for more details.
