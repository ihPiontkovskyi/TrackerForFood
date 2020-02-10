package ua.foodtracker.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HomeModel {
    private DailySums dsto;
    private Integer dailyEnergyGoal;
    private Integer dailyProteinGoal;
    private Integer dailyFatGoal;
    private Integer dailyCarbohydratesGoal;
    private Integer dailyWaterGoal;
    private List<String> labels;
    private List<Integer> energyWeeklyStat;
    private List<Integer> proteinWeeklyStat;
    private List<Integer> fatWeeklyStat;
    private List<Integer> carbWeeklyStat;
    private List<Integer> waterWeeklyStat;
}
