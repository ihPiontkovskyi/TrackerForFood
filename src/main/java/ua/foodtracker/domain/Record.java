package ua.foodtracker.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
public class Record {

    private Integer id;

    @NotNull(message = "meal.in.record.should.be.not.null")
    private Meal meal;

    @PastOrPresent(message = "record.date.should.be.past.or.present")
    @NotNull(message = "date.in.record.should.be.not.null")
    private LocalDate date;

    @NotNull(message = "user.in.record.should.be.not.null")
    private User user;

    @NotNull(message = "meal.weight.should.be.not.null")
    @Positive(message = "meal.weight.not.positive.exception.message")
    private Integer weight;

    public Integer calculateProtein() {
        return (int) (meal.getProtein() * ((double) weight / meal.getWeight()));
    }

    public Integer calculateCarbohydrate() {
        return (int) (meal.getCarbohydrate() * ((double) weight / meal.getWeight()));
    }

    public Integer calculateFat() {
        return (int) (meal.getFat() * ((double) weight / meal.getWeight()));
    }

    public Integer calculateWater() {
        return (int) (meal.getWater() * ((double) weight / meal.getWeight()));
    }

    public Integer calculateEnergy() {
        return (int) (meal.calculateEnergy() * ((double) weight / meal.getWeight()));
    }
}
