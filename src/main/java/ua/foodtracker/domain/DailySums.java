package ua.foodtracker.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public final class DailySums {
    private final int sumEnergy;
    private final int sumProtein;
    private final int sumFat;
    private final int sumCarbohydrates;
    private final int sumWater;
}
