package pixel.task;

/** Represents a task without a structured date or time. */
public class Todo extends Task {
    /** Creates an incomplete ToDo with the specified description.
     *
     * @param description User-visible task description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
