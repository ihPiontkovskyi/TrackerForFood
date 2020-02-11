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
            int value = Integer.parseInt(param);
            if (value < 0 || value > maxValue) {
                return defaultValue;
            }
            return value;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
