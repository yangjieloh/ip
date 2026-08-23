package pixel.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        try {
            LocalDate startDate = parseLeadingDate(from);
            LocalDate endDate;
            try {
                endDate = parseLeadingDate(to);
            } catch (DateTimeParseException exception) {
                endDate = startDate;
            }
            return !date.isBefore(startDate) && !date.isAfter(endDate);
        } catch (DateTimeParseException exception) {
            return false;
        }
    }

    /**
     * Parses an ISO date from the beginning of an Event time string.
     * This accepts values such as {@code 2019-12-02 14:00} and
     * {@code 2019-12-02T14:00} while preserving the existing free-form syntax.
     *
     * @param dateTime Event time text.
     * @return Date at the beginning of the text.
     * @throws DateTimeParseException If no valid ISO date begins the text.
     */
    private static LocalDate parseLeadingDate(String dateTime) {
        if (dateTime.length() < 10) {
            return LocalDate.parse(dateTime);
        }
        return LocalDate.parse(dateTime.substring(0, 10));
    }

    @Override
    public String toDataString() {
        return "E | " + (isDone ? "1" : "0") + " | " + escapeDataField(description)
                + " | " + escapeDataField(from) + " | " + escapeDataField(to);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from + " to: " + to + ")";
    }
}
