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

    @NotNull
    private Meal meal;

    @PastOrPresent
    private LocalDate date;

    @NotNull
    private User user;
}
