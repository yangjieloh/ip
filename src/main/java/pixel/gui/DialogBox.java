package pixel.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Represents one FXML-backed message in the Pixel conversation.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private Label speaker;

    private DialogBox(String text, String speakerName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load a Pixel dialog box.", exception);
        }

        dialog.setText(text);
        speaker.setText(speakerName);
    }

    /**
     * Creates a right-aligned dialog for user input.
     *
     * @param text User message.
     * @return Dialog box representing the user's message.
     */
    public static DialogBox getUserDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "You");
        dialogBox.getStyleClass().add("user-message");
        return dialogBox;
    }

    /**
     * Creates a left-aligned dialog for Pixel's response.
     *
     * @param text Pixel response.
     * @return Dialog box representing Pixel's response.
     */
    public static DialogBox getPixelDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "Pixel");
        dialogBox.flip();
        dialogBox.getStyleClass().add("pixel-message");
        return dialogBox;
    }

    /** Flips the speaker label to the left side for Pixel's messages. */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
    }
}
