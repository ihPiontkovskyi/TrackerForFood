package ua.foodtracker.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class Meal {
    private Integer id;

    @Pattern(regexp = "^[a-zA-zа-яА-Я]{3,32}$", message = "meal.incorrect.name.exception.message")
    private String name;

    @Positive(message = "meal.protein.not.positive.exception.message")
    @NotNull(message = "meal.protein.null.exception.message")
    private Integer protein;

    private User user;

    @Positive(message = "meal.carbohydrate.not.positive.exception.message")
    @NotNull(message = "meal.carbohydrate.null.exception.message")
    private Integer carbohydrate;

    @Positive(message = "meal.fat.not.positive.exception.message")
    @NotNull(message = "meal.fat.null.exception.message")
    private Integer fat;

    @Positive(message = "meal.weight.not.positive.exception.message")
    @NotNull(message = "meal.weight.null.exception.message")
    private Integer weight;

    @Positive(message = "meal.water.not.positive.exception.message")
    @NotNull(message = "meal.water.null.exception.message")
    private Integer water;

    public Integer calculateEnergy() {
        return carbohydrate * 4 + protein * 4 + fat * 9;
    }
}
