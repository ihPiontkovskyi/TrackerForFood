package ua.foodtracker.service.utility;

import ua.foodtracker.service.exception.IncorrectDataException;

import java.util.function.IntFunction;

public class ServiceUtility {
    private static final String INCORRECT_DATA = "incorrect.data";

    private ServiceUtility() {
    }

    public static long getNumberOfPage(long countOfRecords, long itemsPerPage) {
        return countOfRecords % itemsPerPage == 0 ? countOfRecords / itemsPerPage : countOfRecords / itemsPerPage + 1;
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
}
