package ua.foodtracker.controller;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.MealInfo;
import ua.foodtracker.domain.User;
import ua.foodtracker.domain.UserProfileInfo;
import ua.foodtracker.exception.UnauthorizedException;
import ua.foodtracker.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class ControllerHelper {
    //TODO add tests
    public static User getUserFromSecurityContext(UserService service) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedException("unauthorized.request"));
    }

    public static List<String> getUserAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public static boolean isAdminAccessed(Meal meal) {
        return meal.getUser() == null && getUserAuthorities().contains("ADMIN");
    }

    public static boolean isUserAccessed(Meal meal, User user) {
        return Optional.ofNullable(meal)
                .map(Meal::getUser)
                .map(User::getId)
                .filter(x -> user.getId().equals(x))
                .isPresent();
    }

    public static UserProfileInfo getUserProfile(UserService userService) {
        User user = getUserFromSecurityContext(userService);
        UserProfileInfo userProfileInfo = new UserProfileInfo();
        userProfileInfo.setBirthday(user.getBirthday());
        userProfileInfo.setFirstName(user.getFirstName());
        userProfileInfo.setLastName(user.getLastName());
        userProfileInfo.setGender(user.getGender());
        userProfileInfo.setHeight(user.getHeight());
        userProfileInfo.setLifestyle(user.getLifestyle());
        userProfileInfo.setWeight(user.getWeight());
        return userProfileInfo;
    }

    public static User makeChanges(UserProfileInfo userProfileInfo, UserService userService) {
        User user = getUserFromSecurityContext(userService);
        user.setBirthday(userProfileInfo.getBirthday());
        user.setFirstName(userProfileInfo.getFirstName());
        user.setGender(userProfileInfo.getGender());
        user.setHeight(userProfileInfo.getHeight());
        user.setLastName(userProfileInfo.getLastName());
        user.setLifestyle(userProfileInfo.getLifestyle());
        user.setWeight(userProfileInfo.getWeight());
        return user;
    }

    public static MealInfo getMealInfo(Meal meal) {
        return new MealInfo(meal.getId(), meal.getName());
    }
}