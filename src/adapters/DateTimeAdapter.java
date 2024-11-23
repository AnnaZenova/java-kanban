package adapters;

import java.time.format.DateTimeFormatter;

public class DateTimeAdapter {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static DateTimeFormatter getDateTimeFormat() {
        return DATE_TIME_FORMATTER;
    }
}



