package ua.foodtracker.service.utility;

import lombok.experimental.UtilityClass;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.MealEntity;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.exception.IncorrectDataException;

import java.util.function.IntFunction;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@UtilityClass
public class ServiceUtility {
    private static final String INCORRECT_DATA = "incorrect.data";

    public static int getNumberOfPage(long countOfRecords, int itemsPerPage) {
        return ((int) countOfRecords / itemsPerPage) + (countOfRecords % itemsPerPage == 0 ? 0 : 1);
    }

    public static <E> E findByStringParam(String param, IntFunction<E> function) {
        if (param == null) {
            throw new IncorrectDataException(INCORRECT_DATA);
        }
        try {
            return function.apply(Integer.parseInt(param));
        } catch (NumberFormatException ex) {
            throw new IncorrectDataException(INCORRECT_DATA);
        }
    }

    public static boolean isAccessed(User userInSession, MealEntity entity) {
        UserEntity currentUser = entity.getUser();

        return isNull(currentUser) && userInSession.getRole() == Role.ADMIN ||
                nonNull(currentUser) && currentUser.getId().equals(userInSession.getId());
    }
}
