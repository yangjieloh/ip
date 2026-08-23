package pixel.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import pixel.command.AddCommand;
import pixel.command.Command;
import pixel.command.CommandType;
import pixel.command.DateCommand;
import pixel.command.DeleteCommand;
import pixel.command.ExitCommand;
import pixel.command.ListCommand;
import pixel.command.MarkCommand;
import pixel.command.UnmarkCommand;
import pixel.command.UnknownCommand;
import pixel.task.Deadline;
import pixel.task.Event;
import pixel.task.Task;
import pixel.task.Todo;

/**
 * Interprets raw user commands and converts their arguments into domain values.
 */
public class Parser {

    /**
     * Parses a full user command into an executable command object.
     *
     * @param command Full command entered by the user.
     * @return Command ready to execute.
     * @throws IllegalArgumentException If a recognized command has invalid arguments.
     */
    public Command parse(String command) {
        CommandType commandType = parseCommandType(command);
        return switch (commandType) {
        case BYE -> new ExitCommand();
        case LIST -> new ListCommand();
        case DATE -> new DateCommand(parseDate(command));
        case MARK -> new MarkCommand(parseTaskIndex(command, commandType));
        case UNMARK -> new UnmarkCommand(parseTaskIndex(command, commandType));
        case TODO, DEADLINE, EVENT -> new AddCommand(parseTask(command, commandType));
        case DELETE -> new DeleteCommand(parseTaskIndex(command, commandType));
        case UNKNOWN -> new UnknownCommand();
        };
    }

    /**
     * Identifies the type of a command while preserving Pixel's command syntax.
     *
     * @param command Full command entered by the user.
     * @return Matching command type, or {@link CommandType#UNKNOWN} when unrecognized.
     */
    private CommandType parseCommandType(String command) {
        if (command.equals("bye")) {
            return CommandType.BYE;
        } else if (command.equals("list")) {
            return CommandType.LIST;
        } else if (hasKeyword(command, "date")) {
            return CommandType.DATE;
        } else if (hasKeyword(command, "mark")) {
            return CommandType.MARK;
        } else if (hasKeyword(command, "unmark")) {
            return CommandType.UNMARK;
        } else if (hasKeyword(command, "todo")) {
            return CommandType.TODO;
        } else if (hasKeyword(command, "deadline")) {
            return CommandType.DEADLINE;
        } else if (hasKeyword(command, "event")) {
            return CommandType.EVENT;
        } else if (hasKeyword(command, "delete")) {
            return CommandType.DELETE;
        }
        return CommandType.UNKNOWN;
    }

    /**
     * Parses the date argument of a {@code date} command.
     *
     * @param command Full date command.
     * @return Parsed date.
     * @throws IllegalArgumentException If the date is missing or invalid.
     */
    private LocalDate parseDate(String command) {
        try {
            return LocalDate.parse(getArguments(command, "date"));
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Oops! Please enter a valid date after date "
                    + "in YYYY-MM-DD format.", exception);
        }
    }

    /**
     * Parses a one-based task number and converts it to a zero-based index.
     *
     * @param command Full mark, unmark, or delete command.
     * @param commandType Type of the command being parsed.
     * @return Zero-based task index.
     * @throws IllegalArgumentException If the task number is missing or invalid.
     */
    private int parseTaskIndex(String command, CommandType commandType) {
        String keyword = commandType.name().toLowerCase();
        try {
            return Integer.parseInt(getArguments(command, keyword)) - 1;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Please specify a valid task number after "
                    + keyword + ".", exception);
        }
    }

    /**
     * Parses a task-creation command into the corresponding task object.
     *
     * @param command Full task-creation command.
     * @param commandType Type of task to create.
     * @return Parsed task.
     * @throws IllegalArgumentException If required task details are missing or invalid.
     */
    private Task parseTask(String command, CommandType commandType) {
        return switch (commandType) {
        case TODO -> parseTodo(command);
        case DEADLINE -> parseDeadline(command);
        case EVENT -> parseEvent(command);
        default -> throw new IllegalArgumentException(
                "The command does not create a task: " + commandType);
        };
    }

    private Todo parseTodo(String command) {
        String description = getArguments(command, "todo");
        if (description.isEmpty()) {
            throw new IllegalArgumentException(
                    "Oops! Please give me a description for the todo.");
        }
        return new Todo(description);
    }

    private Deadline parseDeadline(String command) {
        String details = getArguments(command, "deadline");
        if (details.isEmpty()) {
            throw new IllegalArgumentException(
                    "Oops! Please give me a description and deadline.");
        } else if (!details.matches("(?s).*(?:^|\\s)/by(?:\\s+.*|$)")) {
            throw new IllegalArgumentException(
                    "Oops! Please specify the deadline using /by.");
        }

        String[] parts = details.split("(?:^|\\s+)/by(?=\\s|$)", 2);
        String description = parts[0].trim();
        String byString = parts[1].trim();
        if (description.isEmpty() || byString.isEmpty()) {
            throw new IllegalArgumentException(
                    "Oops! The deadline description and date cannot be empty.");
        }

        try {
            return new Deadline(description, LocalDate.parse(byString));
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Oops! Please enter the deadline date "
                    + "in YYYY-MM-DD format.", exception);
        }
    }

    private Event parseEvent(String command) {
        String details = getArguments(command, "event");
        if (details.isEmpty()) {
            throw new IllegalArgumentException(
                    "Oops! Please give me an event description and time.");
        } else if (!details.matches("(?s).*(?:^|\\s)/from(?:\\s+.*|$)")) {
            throw new IllegalArgumentException(
                    "Oops! Please specify the event using /from and /to.");
        }

        String[] fromParts = details.split("(?:^|\\s+)/from(?=\\s|$)", 2);
        String[] toParts = fromParts[1].split("(?:^|\\s+)/to(?=\\s|$)", 2);
        if (toParts.length < 2) {
            throw new IllegalArgumentException(
                    "Oops! Please specify the event using /from and /to.");
        } else if (fromParts[0].isBlank() || toParts[0].isBlank()
                || toParts[1].isBlank()) {
            throw new IllegalArgumentException(
                    "Oops! The event description and times cannot be empty.");
        }

        return new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
    }

    private String getArguments(String command, String keyword) {
        if (command.length() <= keyword.length()) {
            return "";
        }
        return command.substring(keyword.length()).trim();
    }

    private boolean hasKeyword(String command, String keyword) {
        return command.equals(keyword)
                || command.length() > keyword.length()
                && command.startsWith(keyword)
                && Character.isWhitespace(command.charAt(keyword.length()));
    }
}
