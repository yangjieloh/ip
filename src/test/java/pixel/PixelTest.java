package pixel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests the response adapter used by the graphical user interface. */
class PixelTest {

    @Test
    void getResponse_todoCommand_returnsAddedTaskMessage(@TempDir Path tempDir) {
        Pixel pixel = new Pixel(tempDir.resolve("pixel.txt"));

        String response = pixel.getResponse("todo read book");

        assertTrue(response.contains("[T][ ] read book"));
        assertTrue(response.contains("Now you have 1 tasks in the list."));
    }

    @Test
    void getResponse_listCommand_reusesTaskList(@TempDir Path tempDir) {
        Pixel pixel = new Pixel(tempDir.resolve("pixel.txt"));
        pixel.getResponse("todo read book");

        String response = pixel.getResponse("list");

        assertEquals("Here are the tasks in your list:" + System.lineSeparator()
                + "1.[T][ ] read book", response);
    }

    @Test
    void getResponse_byeCommand_returnsGoodbyeMessage(@TempDir Path tempDir) {
        Pixel pixel = new Pixel(tempDir.resolve("pixel.txt"));

        String response = pixel.getResponse("bye");

        assertEquals("Bye. Hope to see you again soon!", response);
    }
}
