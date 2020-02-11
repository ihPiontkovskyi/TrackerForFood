package ua.foodtracker.service;

import org.springframework.data.domain.Page;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.User;

import java.util.Optional;

public interface MealService {
    Page<Meal> findAllByPage(String pageNumber);

    int pageCount();

    void add(Meal meal);

    void delete(String id, User user);

    void modify(Meal meal);

    Optional<Meal> findById(String id);
}
