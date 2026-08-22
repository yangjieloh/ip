/**
 * Represents an executable instruction understood by Pixel.
 */
public abstract class Command {

    /**
     * Performs this command's action using the application's collaborators.
     *
     * @param tasks Application task list.
     * @param ui Console user interface.
     * @param storage Task persistence service.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);

    /**
     * Indicates whether Pixel should stop after executing this command.
     *
     * @return {@code true} only for an exit command.
     */
    public boolean isExit() {
        return false;
    }
}
