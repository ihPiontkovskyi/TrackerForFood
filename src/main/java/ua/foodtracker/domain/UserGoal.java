package ua.foodtracker.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@Builder
public class UserGoal {

    private Integer id;

    @Positive
    private Integer dailyEnergyGoal;

    @Positive
    private Integer dailyFatGoal;

    @Positive
    private Integer dailyProteinGoal;

    @Positive
    private Integer dailyCarbohydrateGoal;

    @Positive
    private Integer dailyWaterGoal;
}
