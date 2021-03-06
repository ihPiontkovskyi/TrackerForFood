package ua.foodtracker.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
public class UserProfileInfo {

    @Pattern(regexp = "^[a-zA-zа-яА-Я]{3,32}$", message = "user.first.name.exception.message")
    private String firstName;

    @Pattern(regexp = "^[a-zA-zа-яА-Я]{3,32}$", message = "user.last.name.exception.message")
    private String lastName;

    @Positive(message = "user.weight.not.positive.exception.message")
    @NotNull(message = "user.weight.null.exception.message")
    private Integer weight;

    @Positive(message = "user.height.not.positive.exception.message")
    @NotNull(message = "user.height.null.exception.message")
    private Integer height;

    @NotNull(message = "user.lifestyle.null.exception.message")
    private Lifestyle lifestyle;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "user.birthday.should.be.past.or.present")
    @NotNull(message = "user.birthday.null.exception.message")
    private LocalDate birthday;

    @NotNull(message = "user.gender.null.exception.message")
    private Gender gender;
}
