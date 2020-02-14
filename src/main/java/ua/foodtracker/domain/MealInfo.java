package ua.foodtracker.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MealInfo {
    private final Integer id;
    private final String label;
}
