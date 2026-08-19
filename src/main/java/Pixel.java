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
            } else if (command.equals("todo") || command.startsWith("todo ")){
                String description = "";
                if (command.length() > 4) {
                    description = command.substring(4).trim();
                }
                if (description.isEmpty()) {
                    System.out.println("Oops! Please give me a description for the todo.");
                    System.out.println(line);
                } else {
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    printTaskAdded(tasks[taskCount - 1], taskCount, line);
                }
            } else if (command.equals("deadline") || command.startsWith("deadline ")) {
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
                        tasks[taskCount] = new Deadline(description, by);
                        taskCount++;
                        printTaskAdded(tasks[taskCount - 1], taskCount, line);
                    }
                }
            } else if (command.equals("event") || command.startsWith("event ")) {
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
                        tasks[taskCount] = new Event(description, from, to);
                        taskCount++;
                        printTaskAdded(tasks[taskCount - 1], taskCount, line);
                    }
                }
            } else {
                System.out.println("Sorry, I don't recognise that command.");
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
