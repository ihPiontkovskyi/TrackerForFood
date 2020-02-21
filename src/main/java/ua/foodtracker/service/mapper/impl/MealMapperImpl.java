package ua.foodtracker.service.mapper.impl;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.entity.MealEntity;
import ua.foodtracker.service.mapper.MealMapper;
import ua.foodtracker.service.mapper.UserMapper;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealMapperImpl implements MealMapper {

    private final UserMapper userMapper;

    @Override
    public Meal mapToDomain(MealEntity entity) {
        Meal meal = new Meal();
        meal.setCarbohydrate(entity.getCarbohydrate());
        meal.setFat(entity.getFat());
        meal.setId(entity.getId());
        meal.setName(entity.getName());
        meal.setProtein(entity.getProtein());
        meal.setWater(entity.getWater());
        meal.setWeight(entity.getWeight());
        meal.setUser(userMapper.mapToDomain(entity.getUser()));
        return meal;
    }

    @Override
    public MealEntity mapToEntity(Meal domain) {
        MealEntity entity = new MealEntity();
        entity.setCarbohydrate(domain.getCarbohydrate());
        entity.setFat(domain.getFat());
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setProtein(domain.getProtein());
        entity.setWater(domain.getWater());
        entity.setWeight(domain.getWeight());
        entity.setUser(userMapper.mapToEntity(domain.getUser()));
        return entity;
    }
}
