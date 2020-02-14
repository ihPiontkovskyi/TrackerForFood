package ua.foodtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foodtracker.entity.MealEntity;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, Integer> {
    List<MealEntity> findAllByNameIsStartingWith(String term);
}
