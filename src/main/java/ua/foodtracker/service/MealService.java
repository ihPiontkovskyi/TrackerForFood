package ua.foodtracker.service;

import ua.foodtracker.domain.Meal;

import java.util.List;
import java.util.Optional;

public interface MealService {
    List<Meal> findAllByPage(String pageNumber);

    long pageCount();

    void add(Meal meal);

    void delete(Meal meal);

    void modify(Meal meal);

    Optional<Meal> findById(String id);
}
