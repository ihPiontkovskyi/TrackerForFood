package ua.foodtracker.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ParameterParser {

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
