package ua.foodtracker.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MealInfo {
    private final Integer id;
    private final String label;
}
