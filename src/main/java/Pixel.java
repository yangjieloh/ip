import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Starts the Pixel chatbot application.
 */
public class Pixel {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(Path.of("data", "pixel.txt"));
        Parser parser = new Parser();
        ui.showWelcome();

        ArrayList<String> loadWarnings = new ArrayList<>();
        TaskList tasks = new TaskList(storage.load(loadWarnings));
        for (String warning : loadWarnings) {
            ui.showMessage(warning);
        }
        if (!loadWarnings.isEmpty()) {
            ui.showLine();
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            CommandType commandType = parser.parseCommandType(command);
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
                    LocalDate date = parser.parseDate(command);
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
                } catch (IllegalArgumentException exception) {
                    ui.showMessage(exception.getMessage());
                }
                ui.showLine();
            } else if (commandType == CommandType.MARK) {
                try {
                    int index = parser.parseTaskIndex(command, commandType);
                    if (!tasks.isValidIndex(index)) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        tasks.markAsDone(index);
                        saveTasksSafely(tasks, storage, ui);
                        ui.showMessage("Nice! I've marked this task as done:");
                        ui.showMessage("  " + tasks.get(index));
                    }
                } catch (IllegalArgumentException exception) {
                    ui.showMessage(exception.getMessage());
                }
                ui.showLine();
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int index = parser.parseTaskIndex(command, commandType);
                    if (!tasks.isValidIndex(index)) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        tasks.markAsNotDone(index);
                        saveTasksSafely(tasks, storage, ui);
                        ui.showMessage("OK, I've marked this task as not done yet:");
                        ui.showMessage("  " + tasks.get(index));
                    }
                } catch (IllegalArgumentException exception) {
                    ui.showMessage(exception.getMessage());
                }
                ui.showLine();
            } else if (commandType == CommandType.TODO
                    || commandType == CommandType.DEADLINE
                    || commandType == CommandType.EVENT) {
                try {
                    Task task = parser.parseTask(command, commandType);
                    tasks.add(task);
                    saveTasksSafely(tasks, storage, ui);
                    ui.showTaskAdded(task, tasks.size());
                } catch (IllegalArgumentException exception) {
                    ui.showMessage(exception.getMessage());
                    ui.showLine();
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int index = parser.parseTaskIndex(command, commandType);
                    if (!tasks.isValidIndex(index)) {
                        ui.showMessage("That task number does not exist.");
                    } else {
                        Task deletedTask = tasks.delete(index);
                        saveTasksSafely(tasks, storage, ui);
                        ui.showMessage("Noted. I've removed this task:");
                        ui.showMessage("  " + deletedTask);
                        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                    }
                } catch (IllegalArgumentException exception) {
                    ui.showMessage(exception.getMessage());
                }
                ui.showLine();
            } else {
                ui.showMessage("Sorry, I don't recognise that command.");
                ui.showLine();
            }
        }
    }

    private static void saveTasksSafely(TaskList tasks, Storage storage, Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException | SecurityException exception) {
            ui.showMessage("Oops! I couldn't save your tasks. "
                    + "Your changes will only last until Pixel exits.");
        }
    }
}
