package pixel.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Represents a task that must be completed by a specific date. */
public class Deadline extends Task {
    /** Format used when showing deadlines to users. */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Date by which this task must be completed. */
    protected LocalDate by;

    /** Creates a deadline with the specified description and date.
     *
     * @param description User-visible task description.
     * @param by Date by which the task must be completed.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return by.equals(date);
    }

    /**
     * Formats a date for console output.
     *
     * @param date Date to format.
     * @return Date in the user-facing format.
     */
    public static String formatDate(LocalDate date) {
        return date.format(DISPLAY_DATE_FORMAT);
    }

    @Override
    public String toDataString() {
        return "D | " + (isDone ? "1" : "0") + " | " + escapeDataField(description)
                + " | " + escapeDataField(by.toString());
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + formatDate(by) + ")";
    }
}
