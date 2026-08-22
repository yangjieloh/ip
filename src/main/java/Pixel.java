import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts the Pixel chatbot application.
 */
public class Pixel {
    private static final Path DATA_FILE = Path.of("data", "pixel.txt");

    public static void main(String[] args) {
        String banner = " ____  _          _ \n"
                + "|  _ \\(_)_  _____| |\n"
                + "| |_) | \\ \\/ / _ \\ |\n"
                + "|  __/| |>  <  __/ |\n"
                + "|_|   |_/_/\\_\\___|_|";
        String line = "____________________________________________________________";
        System.out.println(line);
        System.out.println(banner);
        System.out.println("Hello! I'm Pixel.");
        System.out.println("What can I do for you?");
        System.out.println(line);

        ArrayList<String> loadWarnings = new ArrayList<>();
        ArrayList<Task> tasks = loadTasks(loadWarnings);
        for (String warning : loadWarnings) {
            System.out.println(warning);
        }
        if (!loadWarnings.isEmpty()) {
            System.out.println(line);
        }
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            CommandType commandType = CommandType.fromCommand(command);
            System.out.println(line);

            if (commandType == CommandType.BYE) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            } else if (commandType == CommandType.LIST) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + "." + tasks.get(i));
                }
                System.out.println(line);
            } else if (commandType == CommandType.MARK) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(4).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= tasks.size()) {
                        System.out.println("That task number does not exist.");
                    } else {
                        tasks.get(index).markAsDone();
                        saveTasksSafely(tasks);
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks.get(index));
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please specify a valid task number after mark.");
                }
                System.out.println(line);
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(6).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= tasks.size()) {
                        System.out.println("That task number does not exist.");
                    } else {
                        tasks.get(index).markAsNotDone();
                        saveTasksSafely(tasks);
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks.get(index));
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please specify a valid task number after unmark.");
                }
                System.out.println(line);
            } else if (commandType == CommandType.TODO) {
                String description = "";
                if (command.length() > 4) {
                    description = command.substring(4).trim();
                }
                if (description.isEmpty()) {
                    System.out.println("Oops! Please give me a description for the todo.");
                    System.out.println(line);
                } else {
                    tasks.add(new Todo(description));
                    saveTasksSafely(tasks);
                    printTaskAdded(tasks.get(tasks.size() - 1), tasks.size(), line);
                }
            } else if (commandType == CommandType.DEADLINE) {
                String details = "";
                if (command.length() > 8) {
                    details = command.substring(8).trim();
                }
                if (details.isEmpty()) {
                    System.out.println("Oops! Please give me a description and deadline.");
                    System.out.println(line);
                } else if (!details.matches("(?s).*(?:^|\\s)/by(?:\\s+.*|$)")) {
                    System.out.println("Oops! Please specify the deadline using /by.");
                    System.out.println(line);
                } else {
                    String[] parts = details.split("(?:^|\\s+)/by(?=\\s|$)", 2);
                    String description = parts[0].trim();
                    String byString = parts[1].trim();
                    if (description.isEmpty() || byString.isEmpty()) {
                        System.out.println("Oops! The deadline description and date cannot be empty.");
                        System.out.println(line);
                    } else {
                        try {
                            LocalDate by = LocalDate.parse(byString);
                            tasks.add(new Deadline(description, by));
                            saveTasksSafely(tasks);
                            printTaskAdded(tasks.get(tasks.size() - 1), tasks.size(), line);
                        } catch (DateTimeParseException exception) {
                            System.out.println("Oops! Please enter the deadline date "
                                    + "in YYYY-MM-DD format.");
                            System.out.println(line);
                        }
                    }
                }
            } else if (commandType == CommandType.EVENT) {
                String details = "";
                if (command.length() > 5) {
                    details = command.substring(5).trim();
                }
                if (details.isEmpty()) {
                    System.out.println("Oops! Please give me an event description and time.");
                    System.out.println(line);
                } else if (!details.matches("(?s).*(?:^|\\s)/from(?:\\s+.*|$)")) {
                    System.out.println("Oops! Please specify the event using /from and /to.");
                    System.out.println(line);
                } else {
                    String[] fromParts = details.split("(?:^|\\s+)/from(?=\\s|$)", 2);
                    String[] toParts = fromParts[1].split("(?:^|\\s+)/to(?=\\s|$)", 2);
                    if (toParts.length < 2) {
                        System.out.println("Oops! Please specify the event using /from and /to.");
                        System.out.println(line);
                    } else if (fromParts[0].isBlank() || toParts[0].isBlank()
                            || toParts[1].isBlank()) {
                        System.out.println("Oops! The event description and times cannot be empty.");
                        System.out.println(line);
                    } else {
                        tasks.add(new Event(fromParts[0].trim(), toParts[0].trim(),
                                toParts[1].trim()));
                        saveTasksSafely(tasks);
                        printTaskAdded(tasks.get(tasks.size() - 1), tasks.size(), line);
                    }
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(6).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= tasks.size()) {
                        System.out.println("That task number does not exist.");
                    } else {
                        Task deletedTask = tasks.remove(index);
                        saveTasksSafely(tasks);
                        System.out.println("Noted. I've removed this task:");
                        System.out.println("  " + deletedTask);
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please specify a valid task number after delete.");
                }
                System.out.println(line);
            } else {
                System.out.println("Sorry, I don't recognise that command.");
                System.out.println(line);
            }
        }
    }

    /**
     * Writes the current task list to the application's data file.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the data directory or file cannot be written.
     */
    public static void saveTasks(ArrayList<Task> tasks) throws IOException {
        Files.createDirectories(DATA_FILE.getParent());
        ArrayList<String> taskData = new ArrayList<>();
        for (Task task : tasks) {
            taskData.add(task.toDataString());
        }
        Path temporaryFile = DATA_FILE.resolveSibling(DATA_FILE.getFileName() + ".tmp");
        Files.write(temporaryFile, taskData, StandardCharsets.UTF_8);
        try {
            Files.move(temporaryFile, DATA_FILE, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, DATA_FILE, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void saveTasksSafely(ArrayList<Task> tasks) {
        try {
            saveTasks(tasks);
        } catch (IOException | SecurityException exception) {
            System.out.println("Oops! I couldn't save your tasks. "
                    + "Your changes will only last until Pixel exits.");
        }
    }

    /**
     * Loads saved tasks, or returns an empty list when no data file exists yet.
     *
     * @param warnings Messages describing records that could not be loaded.
     * @return Valid tasks restored from the data file.
     */
    public static ArrayList<Task> loadTasks(ArrayList<String> warnings) {
        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<String> savedLines;
        try {
            savedLines = new ArrayList<>(Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8));
        } catch (NoSuchFileException exception) {
            return tasks;
        } catch (IOException | SecurityException exception) {
            warnings.add("Oops! I couldn't read the saved tasks. Starting with an empty list.");
            return tasks;
        }

        for (int i = 0; i < savedLines.size(); i++) {
            String taskData = savedLines.get(i);
            if (taskData.isBlank()) {
                continue;
            }
            try {
                tasks.add(Task.fromDataString(taskData));
            } catch (IllegalArgumentException exception) {
                warnings.add("Oops! I skipped invalid saved task on line " + (i + 1)
                        + ": " + exception.getMessage());
            }
        }
        return tasks;
    }

    public static void printTaskAdded(Task task, int taskCount, String line) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(line);
    }
}
