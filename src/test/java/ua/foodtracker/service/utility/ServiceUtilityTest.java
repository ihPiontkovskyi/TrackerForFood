package ua.foodtracker.service.utility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static ua.foodtracker.service.utility.ServiceUtility.getNumberOfPage;

@RunWith(Parameterized.class)
public class ServiceUtilityTest {

    @Parameter
    public long count;
    @Parameter(1)
    public int items;
    @Parameter(2)
    public int expected;

    @Parameters(name = "count={0}, items={1}, expected={2} ")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {12, 6, 2},
                {13, 6, 3},
                {14, 6, 3},
                {12, 1, 12},
                {0, 6, 0}
        });
    }

    @Test
    public void parseDateOrDefault() {
        assertEquals(expected, getNumberOfPage(count, items));
    }
}