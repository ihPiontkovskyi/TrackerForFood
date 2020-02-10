package ua.foodtracker.utility;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ParameterParser {

    private ParameterParser() {
    }

    public static LocalDate parseOrDefault(String param, LocalDate defaultValue) {
        if (param == null) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(param);
        } catch (DateTimeParseException ex) {
            return defaultValue;
        }
    }

    public static Long parseOrDefault(String param, Long defaultValue) {
        if (param == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(param);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

}
