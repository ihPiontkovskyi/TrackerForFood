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

    public static int parsePageNumber(String param, int defaultValue, int maxValue) {
        if (param == null) {
            return defaultValue;
        }
        try {
            int value = Integer.parseInt(param) - 1;
            if (value <= 0) {
                return 0;
            }
            if (value >= maxValue) {
                return maxValue - 1;
            }
            return value;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
