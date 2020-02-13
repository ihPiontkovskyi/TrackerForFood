package ua.foodtracker.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DateProvider {

    private static final int WEEKS_COUNT = 1;
    private static final int PERIOD = 1;
    private static final int DAYS_COUNT = 8;

    public LocalDate parseOrCurrentDate(String param) {
        if (param == null) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(param);
        } catch (DateTimeParseException ex) {
            return LocalDate.now();
        }
    }

    public List<LocalDate> getLastWeek() {
        return Stream.iterate(LocalDate.now().minusWeeks(WEEKS_COUNT), date -> date.plusDays(PERIOD))
                .limit(DAYS_COUNT)
                .sorted()
                .collect(Collectors.toList());
    }
}
