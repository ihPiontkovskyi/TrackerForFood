package ua.foodtracker.utility;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ParameterPageParserTest {
    @Parameterized.Parameter
    public String param;
    @Parameterized.Parameter(1)
    public int defaultValue;
    @Parameterized.Parameter(2)
    public int maxValue;
    @Parameterized.Parameter(3)
    public int expected;

    @Parameterized.Parameters(name = "param={0}, default={1}, max={2} ,expected={3}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"1", 2, 3, 1},
                {"as", 2, 3, 2},
                {null, 2, 3, 2},
                {"4", 2, 3, 2},
                {"-1", 2, 3, 2},
        });
    }

    @Test
    public void parseDateOrDefault() {
        Assert.assertEquals(expected, ParameterParser.parsePageNumber(param, defaultValue, maxValue));
    }
}
