package ua.foodtracker.domain;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class HomeModelTest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM");
    private static final int WEEKS = 1;
    private static final int PERIOD = 1;
    private static final int COUNT = 8;

    private static final HomeModel model = build();

    @Test
    public void dailyGoalTest() {
        assertEquals(50, model.getDailyGoal().getDailyCarbohydratesGoal());
        assertEquals(60, model.getDailyGoal().getDailyFatGoal());
        assertEquals(70, model.getDailyGoal().getDailyProteinGoal());
        assertEquals(100, model.getDailyGoal().getDailyWaterGoal());
        assertEquals(100, model.getDailyGoal().getDailyEnergyGoal());
    }

    private static HomeModel build() {
        return HomeModel.builder()
                .dailySums(getDailySums())
                .weeklyStats(getDateToDailySumsMap())
                .labels(getLabels())
                .dailyGoal(HomeModel.calculateDailyGoal(getDailySums(), buildUserGoal()))
                .build();
    }

    private static Map<String, DailySums> getDateToDailySumsMap() {
        return getLabels().stream()
                .collect(Collectors.toMap(Function.identity(), string -> getDailySums()));
    }

    private static DailySums getDailySums() {
        return DailySums.builder()
                .sumCarbohydrates(50)
                .sumEnergy(2200)
                .sumFat(60)
                .sumProtein(70)
                .sumWater(2000)
                .build();
    }

    private static List<String> getLabels() {
        return Stream.iterate(LocalDate.now().minusWeeks(WEEKS), day -> day.plusDays(PERIOD))
                .map(DATE_TIME_FORMATTER::format)
                .limit(COUNT)
                .collect(Collectors.toList());
    }

    private static UserGoal buildUserGoal() {
        return UserGoal.builder()
                .dailyCarbohydrateGoal(100)
                .dailyWaterGoal(2000)
                .dailyEnergyGoal(2100)
                .dailyFatGoal(100)
                .dailyProteinGoal(100)
                .id(1)
                .build();
    }
}
