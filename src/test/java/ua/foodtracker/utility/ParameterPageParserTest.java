package ua.foodtracker.utility;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static ua.foodtracker.utility.ParameterParser.parsePageNumber;

@RunWith(Parameterized.class)
public class ParameterPageParserTest {
    @Parameter
    public String param;
    @Parameter(1)
    public int defaultValue;
    @Parameter(2)
    public int maxValue;
    @Parameter(3)
    public int expected;

    @Parameters(name = "param={0}, default={1}, max={2} ,expected={3}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"1", 2, 3, 0},
                {"as", 2, 3, 2},
                {null, 2, 3, 2},
                {"4", 2, 3, 2},
                {"-1", 2, 3, 0},
        });
    }

    @Test
    public void parseDateOrDefault() {
        assertEquals(expected, parsePageNumber(param, defaultValue, maxValue));
    }
}
