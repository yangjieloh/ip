import java.util.Scanner;

/**
 * Handles console input and output for Pixel.
 */
public class Ui {
    private static final String LINE =
            "____________________________________________________________";
    private static final String BANNER = " ____  _          _ \n"
            + "|  _ \\(_)_  _____| |\n"
            + "| |_) | \\ \\/ / _ \\ |\n"
            + "|  __/| |>  <  __/ |\n"
            + "|_|   |_/_/\\_\\___|_|";

    private final Scanner scanner;

    /** Creates a UI connected to the process's standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays Pixel's startup banner and greeting. */
    public void showWelcome() {
        showLine();
        showMessage(BANNER);
        showMessage("Hello! I'm Pixel.");
        showMessage("What can I do for you?");
        showLine();
    }

    /** @return Whether another command is available from standard input. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** @return The next command entered by the user. */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Displays one message, followed by a line break. */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /** Displays the separator used between command responses. */
    public void showLine() {
        showMessage(LINE);
    }

    /** Displays the standard confirmation after a task is added. */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Got it. I've added this task:");
        showMessage("  " + task);
        showMessage("Now you have " + taskCount + " tasks in the list.");
    }
}
