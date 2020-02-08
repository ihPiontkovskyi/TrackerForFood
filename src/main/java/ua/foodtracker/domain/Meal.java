package ua.foodtracker.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@Builder
public class Meal {
    private Integer id;

    @Pattern(regexp = "^[a-zA-zа-яА-Я]{3,32}$")
    private String name;

    @Positive
    private Integer protein;

    private User user;

    @Positive
    private Integer carbohydrate;

    @Positive
    private Integer fat;

    @Positive
    private Integer weight;

    @Positive
    private Integer water;
}
