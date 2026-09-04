package pixel.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Owns the application's in-memory collection of tasks and its list operations.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks in the same order.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(List<Task> tasks) {
        assert tasks != null : "initial tasks must not be null";
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return Number of tasks currently stored.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether a zero-based index identifies an existing task.
     *
     * @param index Zero-based task index.
     * @return Whether the index is valid.
     */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index Zero-based task index.
     * @return Task at the index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        assert task != null : "task must not be null";
        tasks.add(task);
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index Zero-based task index.
     * @return Removed task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at a zero-based index as done.
     *
     * @param index Zero-based task index.
     */
    public void markAsDone(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at a zero-based index as not done.
     *
     * @param index Zero-based task index.
     */
    public void markAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
    }
}
