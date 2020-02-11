package ua.foodtracker.utility;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ParameterDateParserTest {
    @Parameterized.Parameter
    public String param;
    @Parameterized.Parameter(1)
    public LocalDate defaultValue;
    @Parameterized.Parameter(2)
    public LocalDate expected;

    @Parameterized.Parameters(name = "param={0}, default={1}, expected={2}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"2020-01-01", LocalDate.of(2020, 10, 8), LocalDate.of(2020, 1, 1)},
                {"a", LocalDate.of(2020, 10, 8), LocalDate.of(2020, 10, 8)},
                {null, LocalDate.of(2020, 10, 8), LocalDate.of(2020, 10, 8)},
                {"01-01-2020", LocalDate.of(2020, 10, 8), LocalDate.of(2020, 10, 8)},
                {"2020-01-01", LocalDate.of(2020, 10, 8), LocalDate.of(2020, 1, 1)},
                {"1999-01-01", LocalDate.of(2020, 10, 8), LocalDate.of(1999, 1, 1)},
        });
    }

    @Test
    public void parseDateOrDefault() {
        Assert.assertEquals(expected, ParameterParser.parseOrDefault(param, defaultValue));
    }
}
