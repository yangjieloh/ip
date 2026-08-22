/**
 * Responds to an unrecognized user command.
 */
public class UnknownCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage("Sorry, I don't recognise that command.");
    }
}
