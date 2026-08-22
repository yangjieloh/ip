import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Starts the Pixel chatbot application.
 */
public class Pixel {
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private final TaskList tasks;
    private final ArrayList<String> loadWarnings;

    /**
     * Creates a Pixel chatbot backed by the specified data file.
     *
     * @param filePath Path to the task data file.
     */
    public Pixel(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();
        loadWarnings = new ArrayList<>();
        tasks = new TaskList(storage.load(loadWarnings));
    }

    /** Starts Pixel's console command loop. */
    public void run() {
        ui.showWelcome();
        for (String warning : loadWarnings) {
            ui.showMessage(warning);
        }
        if (!loadWarnings.isEmpty()) {
            ui.showLine();
        }

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();
            ui.showLine();
            try {
                Command command = parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (IllegalArgumentException exception) {
                ui.showMessage(exception.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /** Starts Pixel using the default relative, OS-independent data path. */
    public static void main(String[] args) {
        new Pixel(Path.of("data", "pixel.txt")).run();
    }
}
