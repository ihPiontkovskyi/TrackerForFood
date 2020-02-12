package ua.foodtracker.controller;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.User;
import ua.foodtracker.exception.UnauthorizedException;
import ua.foodtracker.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class ControllerHelper {

    public static User getUserFromSecurityContext(UserService service) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = service.findByEmail(username);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UnauthorizedException("unauthorized.request");
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
        return meal.getUser() != null && meal.getUser().getId().equals(user.getId());
    }
}