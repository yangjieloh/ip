/**
 * Represents the commands understood by Pixel.
 */
public enum CommandType {
    BYE,
    LIST,
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
        } else if (command.startsWith("mark ")) {
            return MARK;
        } else if (command.startsWith("unmark ")) {
            return UNMARK;
        } else if (command.equals("todo") || command.startsWith("todo ")) {
            return TODO;
        } else if (command.equals("deadline") || command.startsWith("deadline ")) {
            return DEADLINE;
        } else if (command.equals("event") || command.startsWith("event ")) {
            return EVENT;
        } else if (command.startsWith("delete ")) {
            return DELETE;
        }
        return UNKNOWN;
    }
}
