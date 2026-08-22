import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Starts the Pixel chatbot application.
 */
public class Pixel {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(Path.of("data", "pixel.txt"));
        ui.showWelcome();

        ArrayList<String> loadWarnings = new ArrayList<>();
        ArrayList<Task> tasks = storage.load(loadWarnings);
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
                        saveTasksSafely(tasks, storage, ui);
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
                        saveTasksSafely(tasks, storage, ui);
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
                    saveTasksSafely(tasks, storage, ui);
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
                            saveTasksSafely(tasks, storage, ui);
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
                        saveTasksSafely(tasks, storage, ui);
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
                        saveTasksSafely(tasks, storage, ui);
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

    private static void saveTasksSafely(ArrayList<Task> tasks, Storage storage, Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException | SecurityException exception) {
            ui.showMessage("Oops! I couldn't save your tasks. "
                    + "Your changes will only last until Pixel exits.");
        }
    }
}
