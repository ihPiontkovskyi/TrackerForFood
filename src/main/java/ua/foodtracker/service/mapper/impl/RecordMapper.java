package ua.foodtracker.service.mapper.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.Record;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.MealEntity;
import ua.foodtracker.entity.RecordEntity;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.service.mapper.Mapper;


@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecordMapper implements Mapper<Record, RecordEntity> {

    private final Mapper<User, UserEntity> userMapper;
    private final Mapper<Meal, MealEntity> mealMapper;

    @Override
    public Record mapToDomain(RecordEntity entity) {
        return Record.builder()
                .date(entity.getDate())
                .id(entity.getId())
                .meal(mealMapper.mapToDomain(entity.getMeal()))
                .user(userMapper.mapToDomain(entity.getUser()))
                .weight(entity.getWeight())
                .build();
    }

    @Override
    public RecordEntity mapToEntity(Record domain) {
        RecordEntity entity = new RecordEntity();
        entity.setDate(domain.getDate());
        entity.setId(domain.getId());
        entity.setWeight(domain.getWeight());
        entity.setMeal(mealMapper.mapToEntity(domain.getMeal()));
        entity.setUser(userMapper.mapToEntity(domain.getUser()));
        return entity;
    }

}
