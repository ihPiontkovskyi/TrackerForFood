package ua.foodtracker.service;

import org.springframework.data.domain.Page;
import ua.foodtracker.domain.Meal;

import java.util.Optional;

public interface MealService {
    Page<Meal> findAllByPage(String pageNumber);

    long pageCount();

    void add(Meal meal);

    void delete(Meal meal);

    void modify(Meal meal);

    Optional<Meal> findById(String id);
}
