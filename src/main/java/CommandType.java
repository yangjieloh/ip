/**
 * Represents the commands understood by Pixel.
 */
public enum CommandType {
    BYE,
    LIST,
    DATE,
    MARK,
    UNMARK,
    TODO,
    DEADLINE,
    EVENT,
    DELETE,
    UNKNOWN;

    /**
     * Identifies the type of a command while preserving Pixel's command syntax.
     *
     * @param command Full command entered by the user.
     * @return Matching command type, or {@link #UNKNOWN} when it is not recognized.
     */
    public static CommandType fromCommand(String command) {
        if (command.equals("bye")) {
            return BYE;
        } else if (command.equals("list")) {
            return LIST;
        } else if (hasKeyword(command, "date")) {
            return DATE;
        } else if (hasKeyword(command, "mark")) {
            return MARK;
        } else if (hasKeyword(command, "unmark")) {
            return UNMARK;
        } else if (hasKeyword(command, "todo")) {
            return TODO;
        } else if (hasKeyword(command, "deadline")) {
            return DEADLINE;
        } else if (hasKeyword(command, "event")) {
            return EVENT;
        } else if (hasKeyword(command, "delete")) {
            return DELETE;
        }
        return UNKNOWN;
    }

    /**
     * Checks for a command keyword followed by either nothing or whitespace.
     * This prevents inputs such as {@code todoList} from being treated as {@code todo}.
     *
     * @param command Full command entered by the user.
     * @param keyword Keyword to find at the start of the command.
     * @return Whether the command starts with the complete keyword.
     */
    private static boolean hasKeyword(String command, String keyword) {
        return command.equals(keyword)
                || command.length() > keyword.length()
                && command.startsWith(keyword)
                && Character.isWhitespace(command.charAt(keyword.length()));
    }
}
