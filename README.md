# PlugCLI 🚀

A lightweight, extensible, and dynamic Command Line Interface (CLI) framework for Java. 
PlugCLI allows developers to easily build modular CLI commands that are loaded dynamically at runtime.

---

## 💻 1. Installing PlugCLI (For End Users)

If you just want to use the framework on your Windows machine, open your PowerShell terminal and run:

```powershell
iwr -useb https://raw.githubusercontent.com/AKSHITA142/plugCLI/main/install.ps1 | iex
```
*After running, restart your terminal and type `plugcli help`.*

---

## 🛠️ 2. Building Plugins (For Developers)

Want to create your own commands for PlugCLI? It is incredibly easy!

### Option A: The Fast Way (Scaffolding)
If you already have PlugCLI installed, simply navigate to an empty folder and run:
```powershell
plugcli init my-new-plugin
```
This will automatically generate the folder structure, a sample command, and the `build-plugin.ps1` script!

### Option B: The Maven Way (Enterprise)
If you are building a complex plugin and want to use Maven, add PlugCLI as a dependency in your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.akshita142</groupId>
        <artifactId>plugcli-core</artifactId>
        <version>1.1.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

**Automate the JAR output:**
To ensure your plugin is automatically saved into the `plugins/` directory so PlugCLI can find it, add this to your `<build>` section:

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

### Writing Your First Plugin
Just create a Java class that implements `Plugin` and use the `@Command` annotation!

```java
import com.cli.Plugin;
import com.cli.Command;
import com.cli.PlugLogger;

@Command(value = "hello", description = "Prints a greeting")
public class HelloCommand implements Plugin {
    @Override
    public void execute(String[] args) {
        PlugLogger.info("Hello from my custom plugin!");
    }
}
```
Compile using `mvn clean package` and run `plugcli hello`!
