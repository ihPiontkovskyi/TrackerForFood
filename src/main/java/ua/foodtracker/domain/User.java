package ua.foodtracker.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder()
public class User {

    private Integer id;

    @Email
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{4,32}$")
    private String password;

    @Pattern(regexp = "^[a-zA-zа-яА-Я]{3,32}$")
    private String firstName;

    @Pattern(regexp = "^[a-zA-zа-яА-Я]{3,32}$")
    private String lastName;

    @Positive
    private Integer weight;

    @Positive
    private Integer height;

    @NotNull
    private Lifestyle lifestyle;

    @PastOrPresent
    private LocalDate birthday;

    @NotNull
    private Gender gender;

    private UserGoal userGoal;

    @NotNull
    private Role role;
}
