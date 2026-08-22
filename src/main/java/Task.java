/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not completed. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the representation used when saving this task to disk.
     *
     * @return Serialized task data.
     */
    public String toDataString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Recreates a task from one line of saved data.
     *
     * @param data Serialized task data.
     * @return Task represented by the saved data.
     */
    public static Task fromDataString(String data) {
        String[] fields = data.split(" \\| ", -1);
        Task task;
        switch (fields[0]) {
        case "T":
            task = new Todo(fields[2]);
            break;
        case "D":
            task = new Deadline(fields[2], fields[3]);
            break;
        case "E":
            task = new Event(fields[2], fields[3], fields[4]);
            break;
        default:
            throw new IllegalArgumentException("Unknown task type: " + fields[0]);
        }

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
