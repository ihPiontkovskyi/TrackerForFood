package ua.foodtracker.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
public class HomeModel {
    private static final int PERCENTAGE = 100;
    private static final int MAX_PERCENTAGE = 100;

    private DailySums dailySums;
    private List<String> labels;
    private Map<String, DailySums> weeklyStats;
    private DailyGoal dailyGoal;

    public List<Integer> getWeeklyEnergyStat() {
        return getListByFunction(DailySums::getSumEnergy);
    }

    public List<Integer> getWeeklyFatStat() {
        return getListByFunction(DailySums::getSumFat);
    }

    public List<Integer> getWeeklyWaterStat() {
        return getListByFunction(DailySums::getSumWater);
    }

    public List<Integer> getWeeklyCarbohydrateStat() {
        return getListByFunction(DailySums::getSumCarbohydrates);
    }

    public List<Integer> getWeeklyProteinStat() {
        return getListByFunction(DailySums::getSumProtein);
    }

    private List<Integer> getListByFunction(Function<? super DailySums, ? extends Integer> function) {
        return weeklyStats.values().stream().map(function).collect(Collectors.toList());
    }

    public static DailyGoal calculateDailyGoal(DailySums dailySums, UserGoal userGoal) {
        return DailyGoal.builder()
                .dailyCarbohydratesGoal(getPercentage(dailySums.getSumCarbohydrates(), userGoal.getDailyCarbohydrateGoal()))
                .dailyEnergyGoal(getPercentage(dailySums.getSumEnergy(), userGoal.getDailyEnergyGoal()))
                .dailyFatGoal(getPercentage(dailySums.getSumFat(), userGoal.getDailyFatGoal()))
                .dailyProteinGoal(getPercentage(dailySums.getSumProtein(), userGoal.getDailyProteinGoal()))
                .dailyWaterGoal(getPercentage(dailySums.getSumWater(), userGoal.getDailyWaterGoal()))
                .build();
    }

    private static int getPercentage(double dailySum, Integer dailyGoal) {
        return Math.min((int) ((dailySum / dailyGoal) * PERCENTAGE), MAX_PERCENTAGE);
    }

    @Builder
    @Getter
    private static class DailyGoal {
        private Integer dailyEnergyGoal;
        private Integer dailyProteinGoal;
        private Integer dailyFatGoal;
        private Integer dailyCarbohydratesGoal;
        private Integer dailyWaterGoal;
    }
}
