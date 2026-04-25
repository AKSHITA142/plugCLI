import com.cli.Plugin;
import com.cli.Command;

@Command(value = "test", version = "1.0")

/*
 * === OOP CONCEPTS USED ===
 * 1. INHERITANCE → "implements Plugin" — TestPlugin inherits the contract from the Plugin interface and provides concrete behavior.
 * 2. POLYMORPHISM → @Override on getCommand() and execute() — this class provides its own version of these methods (runtime polymorphism).
 * 3. ABSTRACTION → @Command annotation separates metadata from logic.
 */
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
