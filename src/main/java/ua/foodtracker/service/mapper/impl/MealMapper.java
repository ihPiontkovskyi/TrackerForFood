package ua.foodtracker.service.mapper.impl;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.MealEntity;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.service.mapper.Mapper;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealMapper implements Mapper<Meal, MealEntity> {

    private final Mapper<User, UserEntity> userMapper;

    @Override
    public Meal mapToDomain(MealEntity entity) {
        return Meal.builder()
                .carbohydrate(entity.getCarbohydrate())
                .fat(entity.getFat())
                .id(entity.getId())
                .name(entity.getName())
                .protein(entity.getProtein())
                .user(userMapper.mapToDomain(entity.getUser()))
                .water(entity.getWater())
                .weight(entity.getWeight())
                .build();
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
        entity.setWeight(domain.getProtein());
        entity.setUser(userMapper.mapToEntity(domain.getUser()));
        return entity;
    }
}
