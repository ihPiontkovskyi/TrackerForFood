package ua.foodtracker.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class DailySums {
    private final int sumEnergy;
    private final int sumProtein;
    private final int sumFat;
    private final int sumCarbohydrates;
    private final int sumWater;
}
