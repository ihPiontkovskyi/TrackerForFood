package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.entity.MealEntity;
import ua.foodtracker.repository.MealRepository;
import ua.foodtracker.service.MealService;
import ua.foodtracker.utility.Mapper;

import java.util.List;
import java.util.Optional;

import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.ServiceUtility.getNumberOfPage;
import static ua.foodtracker.utility.Mapper.mapMealDomainToMealEntity;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealServiceImpl implements MealService {
    private static final long ITEMS_PER_PAGE = 3L;
    private final MealRepository mealRepository;


    @Override
    public List<Meal> findAllByPage(String pageNumber) {
        return null;
    }

    @Override
    public long pageCount() {
        return getNumberOfPage(mealRepository.count(), ITEMS_PER_PAGE);
    }

    @Override
    public void add(Meal meal) {
        mealRepository.save(mapMealDomainToMealEntity(meal));
    }

    @Override
    public void delete(Meal meal) {
        mealRepository.delete(mapMealDomainToMealEntity(meal));
    }

    @Override
    public void modify(Meal meal) {
        mealRepository.save(mapMealDomainToMealEntity(meal));
    }

    @Override
    public Optional<Meal> findById(String id) {
        return findByStringParam(id, mealRepository::findById).map(Mapper::mapMealEntityToMealDomain);
    }
}
