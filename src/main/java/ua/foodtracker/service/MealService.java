package ua.foodtracker.service;

import org.springframework.data.domain.Page;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.User;

import java.util.List;

public interface MealService {
    Page<Meal> findAllByPage(String pageNumber);

    Meal findById(String id);

    List<Meal> findAllByNameStartWith(String term);

    int pageCount();

    void add(Meal meal);

    void delete(String id, User user);

    void modify(Meal meal);
}
