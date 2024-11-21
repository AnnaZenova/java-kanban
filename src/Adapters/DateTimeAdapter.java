package Adapters;

import java.time.format.DateTimeFormatter;

public class DateTimeAdapter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");

    public static DateTimeFormatter getDateTimeFormat() {
        return DATE_TIME_FORMATTER;
    }
}



