package pixel.command;

import pixel.storage.Storage;
import pixel.task.TaskList;
import pixel.ui.Ui;

/**
 * Ends the current Pixel session.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage("Bye. Hope to see you again soon!");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
