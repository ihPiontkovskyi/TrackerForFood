package ua.foodtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foodtracker.entity.MealEntity;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, Integer> {

}
