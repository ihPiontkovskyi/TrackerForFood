package ua.foodtracker.service.mapper.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foodtracker.domain.Record;
import ua.foodtracker.entity.RecordEntity;
import ua.foodtracker.service.mapper.MealMapper;
import ua.foodtracker.service.mapper.RecordMapper;
import ua.foodtracker.service.mapper.UserMapper;


@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecordMapperImpl implements RecordMapper {

    private final UserMapper userMapper;
    private final MealMapper mealMapper;

    @Override
    public Record mapToDomain(RecordEntity domain) {
        Record record = new Record();
        record.setDate(domain.getDate());
        record.setId(domain.getId());
        record.setWeight(domain.getWeight());
        record.setMeal(mealMapper.mapToDomain(domain.getMeal()));
        record.setUser(userMapper.mapToDomain(domain.getUser()));
        return record;
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
