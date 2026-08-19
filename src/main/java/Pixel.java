import java.util.Scanner;

/**
 * Starts the Pixel chatbot application.
 */
public class Pixel {
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

        Task[] tasks = new Task[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(line);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            } else if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                System.out.println(line);
            } else if (command.startsWith("mark ")) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(5).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= taskCount) {
                        System.out.println("That task number does not exist.");
                    } else {
                        tasks[index].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks[index]);
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please specify a valid task number after mark.");
                }
                System.out.println(line);
            } else if (command.startsWith("unmark ")) {
                try {
                    int taskNumber = Integer.parseInt(command.substring(7).trim());
                    int index = taskNumber - 1;
                    if (index < 0 || index >= taskCount) {
                        System.out.println("That task number does not exist.");
                    } else {
                        tasks[index].markAsNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks[index]);
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please specify a valid task number after unmark.");
                }
                System.out.println(line);
            } else if (command.startsWith("todo ")){
                String description = command.substring(5);
                tasks[taskCount] = new Todo(description);
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount, line);
            } else if (command.startsWith("deadline ")) {
                String details = command.substring(9);
                String[] parts = details.split(" /by ", 2);
                if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
                    System.out.println("Please use: deadline DESCRIPTION /by DATE/TIME");
                    System.out.println(line);
                } else {
                    String description = parts[0];
                    String by = parts[1];
                    tasks[taskCount] = new Deadline(description, by);
                    taskCount++;
                    printTaskAdded(tasks[taskCount - 1], taskCount, line);
                }
            } else if (command.startsWith("event ")) {
                String details = command.substring(6);
                String[] fromParts = details.split(" /from ", 2);
                if (fromParts.length < 2) {
                    System.out.println("Please use: event DESCRIPTION /from START /to END");
                    System.out.println(line);
                } else {
                    String description = fromParts[0];
                    String[] toParts = fromParts[1].split(" /to ", 2);
                    if (toParts.length < 2 || description.isBlank()
                            || toParts[0].isBlank() || toParts[1].isBlank()) {
                        System.out.println("Please use: event DESCRIPTION /from START /to END");
                        System.out.println(line);
                    } else {
                        String from = toParts[0];
                        String to = toParts[1];
                        tasks[taskCount] = new Event(description, from, to);
                        taskCount++;
                        printTaskAdded(tasks[taskCount - 1], taskCount, line);
                    }
                }
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("added: " + command);
                System.out.println(line);
            }
        }
    }

    public static void printTaskAdded(Task task, int taskCount, String line) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(line);
    }
}
