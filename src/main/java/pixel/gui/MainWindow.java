package pixel.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pixel.Pixel;

/**
 * Controls the main Pixel GUI defined in {@code MainWindow.fxml}.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Pixel pixel;

    /**
     * Binds the scroll position to the dialog container so new messages remain visible.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the application logic used to process GUI commands.
     *
     * @param pixel Pixel application instance shared by the GUI.
     */
    public void setPixel(Pixel pixel) {
        this.pixel = pixel;
        dialogContainer.getChildren().add(DialogBox.getPixelDialog(
                "Hello! I'm Pixel.\nWhat can I do for you?"));
    }

    /**
     * Sends the current text to Pixel and appends both sides of the conversation.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = pixel.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getPixelDialog(response));
        userInput.clear();

        if ("bye".equals(input.trim())) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
