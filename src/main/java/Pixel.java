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

/**
 * Starts the Pixel chatbot application.
 */
public class Pixel {
    private static final Path DATA_FILE = Path.of("data", "pixel.txt");

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        ArrayList<String> loadWarnings = new ArrayList<>();
        ArrayList<Task> tasks = loadTasks(loadWarnings);
        for (String warning : loadWarnings) {
            ui.showMessage(warning);
        }
        if (!loadWarnings.isEmpty()) {
            ui.showLine();
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            CommandType commandType = CommandType.fromCommand(command);
            ui.showLine();

            if (commandType == CommandType.BYE) {
                ui.showMessage("Bye. Hope to see you again soon!");
                ui.showLine();
                break;
            } else if (commandType == CommandType.LIST) {
                ui.showMessage("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    ui.showMessage((i + 1) + "." + tasks.get(i));
                }
                ui.showLine();
            } else if (commandType == CommandType.DATE) {
                try {
                    LocalDate date = LocalDate.parse(command.substring(4).trim());
                    boolean hasMatchingTask = false;
                    for (int i = 0; i < tasks.size(); i++) {
                        if (tasks.get(i).occursOn(date)) {
                            if (!hasMatchingTask) {
                                ui.showMessage("Here are the tasks occurring on "
                                        + Deadline.formatDate(date) + ":");
                            }
                            ui.showMessage((i + 1) + "." + tasks.get(i));
                            hasMatchingTask = true;
                        }
                    }
                    if (!hasMatchingTask) {
                        ui.showMessage("There are no tasks occurring on "
                                + Deadline.formatDate(date) + ".");
                    }
                } catch (DateTimeParseException exception) {
                    ui.showMessage("Oops! Please enter a valid date after date "
                            + "in YYYY-MM-DD format.");
                }
                ui.showLine();
            } else if (commandType == CommandType.MARK) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(4).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= tasks.size()) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        tasks.get(index).markAsDone();
                        saveTasksSafely(tasks, ui);
                        ui.showMessage("Nice! I've marked this task as done:");
                        ui.showMessage("  " + tasks.get(index));
                    }
                } catch (NumberFormatException exception) {
                    ui.showMessage("Please specify a valid task number after mark.");
                }
                ui.showLine();
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(6).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= tasks.size()) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        tasks.get(index).markAsNotDone();
                        saveTasksSafely(tasks, ui);
                        ui.showMessage("OK, I've marked this task as not done yet:");
                        ui.showMessage("  " + tasks.get(index));
                    }
                } catch (NumberFormatException exception) {
                    ui.showMessage("Please specify a valid task number after unmark.");
                }
                ui.showLine();
            } else if (commandType == CommandType.TODO) {
                String description = "";
                if (command.length() > 4) {
                    description = command.substring(4).trim();
                }
                if (description.isEmpty()) {
                    ui.showMessage("Oops! Please give me a description for the todo.");
                    ui.showLine();
                } else {
                    tasks.add(new Todo(description));
                    saveTasksSafely(tasks, ui);
                    ui.showTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                }
            } else if (commandType == CommandType.DEADLINE) {
                String details = "";
                if (command.length() > 8) {
                    details = command.substring(8).trim();
                }
                if (details.isEmpty()) {
                    ui.showMessage("Oops! Please give me a description and deadline.");
                    ui.showLine();
                } else if (!details.matches("(?s).*(?:^|\\s)/by(?:\\s+.*|$)")) {
                    ui.showMessage("Oops! Please specify the deadline using /by.");
                    ui.showLine();
                } else {
                    String[] parts = details.split("(?:^|\\s+)/by(?=\\s|$)", 2);
                    String description = parts[0].trim();
                    String byString = parts[1].trim();
                    if (description.isEmpty() || byString.isEmpty()) {
                        ui.showMessage("Oops! The deadline description and date cannot be empty.");
                        ui.showLine();
                    } else {
                        try {
                            LocalDate by = LocalDate.parse(byString);
                            tasks.add(new Deadline(description, by));
                            saveTasksSafely(tasks, ui);
                            ui.showTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        } catch (DateTimeParseException exception) {
                            ui.showMessage("Oops! Please enter the deadline date "
                                    + "in YYYY-MM-DD format.");
                            ui.showLine();
                        }
                    }
                }
            } else if (commandType == CommandType.EVENT) {
                String details = "";
                if (command.length() > 5) {
                    details = command.substring(5).trim();
                }
                if (details.isEmpty()) {
                    ui.showMessage("Oops! Please give me an event description and time.");
                    ui.showLine();
                } else if (!details.matches("(?s).*(?:^|\\s)/from(?:\\s+.*|$)")) {
                    ui.showMessage("Oops! Please specify the event using /from and /to.");
                    ui.showLine();
                } else {
                    String[] fromParts = details.split("(?:^|\\s+)/from(?=\\s|$)", 2);
                    String[] toParts = fromParts[1].split("(?:^|\\s+)/to(?=\\s|$)", 2);
                    if (toParts.length < 2) {
                        ui.showMessage("Oops! Please specify the event using /from and /to.");
                        ui.showLine();
                    } else if (fromParts[0].isBlank() || toParts[0].isBlank()
                            || toParts[1].isBlank()) {
                        ui.showMessage("Oops! The event description and times cannot be empty.");
                        ui.showLine();
                    } else {
                        tasks.add(new Event(fromParts[0].trim(), toParts[0].trim(),
                                toParts[1].trim()));
                        saveTasksSafely(tasks, ui);
                        ui.showTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                    }
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(6).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= tasks.size()) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        Task deletedTask = tasks.remove(index);
                        saveTasksSafely(tasks, ui);
                        ui.showMessage("Noted. I've removed this task:");
                        ui.showMessage("  " + deletedTask);
                        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                    }
                } catch (NumberFormatException exception) {
                    ui.showMessage("Please specify a valid task number after delete.");
                }
                ui.showLine();
            } else {
                ui.showMessage("Sorry, I don't recognise that command.");
                ui.showLine();
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

    private static void saveTasksSafely(ArrayList<Task> tasks, Ui ui) {
        try {
            saveTasks(tasks);
        } catch (IOException | SecurityException exception) {
            ui.showMessage("Oops! I couldn't save your tasks. "
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

}
