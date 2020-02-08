package ua.foodtracker.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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
}
