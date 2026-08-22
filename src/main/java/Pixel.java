import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts the Pixel chatbot application.
 */
public class Pixel {
    private static final Path DATA_FILE = Path.of("data", "pixel.txt");

    public static void main(String[] args) throws IOException {
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

        ArrayList<Task> tasks = loadTasks();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
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
                    int taskNumber = Integer.parseInt(command.substring(5).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= tasks.size()) {
                        System.out.println("That task number does not exist.");
                    } else {
                        tasks.get(index).markAsDone();
                        saveTasks(tasks);
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks.get(index));
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please specify a valid task number after mark.");
                }
                System.out.println(line);
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(7).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= tasks.size()) {
                        System.out.println("That task number does not exist.");
                    } else {
                        tasks.get(index).markAsNotDone();
                        saveTasks(tasks);
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
                    saveTasks(tasks);
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
                } else if (!details.contains(" /by ")) {
                    System.out.println("Oops! Please specify the deadline using /by.");
                    System.out.println(line);
                } else {
                    String[] parts = details.split(" /by ", 2);
                    String description = parts[0].trim();
                    String by = parts[1].trim();
                    if (description.isEmpty() || by.isEmpty()) {
                        System.out.println("Oops! The deadline description and date cannot be empty.");
                        System.out.println(line);
                    } else {
                        tasks.add(new Deadline(description, by));
                        saveTasks(tasks);
                        printTaskAdded(tasks.get(tasks.size() - 1), tasks.size(), line);
                    }
                }
            } else if (commandType == CommandType.EVENT) {
                String details = "";
                if (command.length() > 5) {
                    details = command.substring(5).trim();
                }
                int fromIndex = details.indexOf("/from");
                int toIndex = fromIndex < 0 ? -1 : details.indexOf("/to", fromIndex + 5);
                if (details.isEmpty()) {
                    System.out.println("Oops! Please give me an event description and time.");
                    System.out.println(line);
                } else if (fromIndex < 0 || toIndex < 0) {
                    System.out.println("Oops! Please specify the event using /from and /to.");
                    System.out.println(line);
                } else {
                    String description = details.substring(0, fromIndex).trim();
                    String from = details.substring(fromIndex + 5, toIndex).trim();
                    String to = details.substring(toIndex + 3).trim();
                    if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        System.out.println("Oops! The event description and times cannot be empty.");
                        System.out.println(line);
                    } else {
                        tasks.add(new Event(description, from, to));
                        saveTasks(tasks);
                        printTaskAdded(tasks.get(tasks.size() - 1), tasks.size(), line);
                    }
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(7).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= tasks.size()) {
                        System.out.println("That task number does not exist.");
                    } else {
                        Task deletedTask = tasks.remove(index);
                        saveTasks(tasks);
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
        Files.write(DATA_FILE, taskData);
    }

    /**
     * Loads saved tasks, or returns an empty list when no data file exists yet.
     *
     * @return Tasks restored from the data file.
     * @throws IOException If an existing data file cannot be read.
     */
    public static ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }

        for (String taskData : Files.readAllLines(DATA_FILE)) {
            tasks.add(Task.fromDataString(taskData));
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
