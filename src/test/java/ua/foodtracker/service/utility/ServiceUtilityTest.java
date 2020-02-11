package ua.foodtracker.service.utility;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ServiceUtilityTest {

    @Parameterized.Parameter
    public long count;
    @Parameterized.Parameter(1)
    public int items;
    @Parameterized.Parameter(2)
    public int expected;

    @Parameterized.Parameters(name = "count={0}, items={1}, expected={2} ")
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
        Assert.assertEquals(expected, ServiceUtility.getNumberOfPage(count, items));
    }
}