package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.repository.MealRepository;
import ua.foodtracker.service.MealService;
import ua.foodtracker.utility.Mapper;

import java.util.Optional;

import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.ServiceUtility.getNumberOfPage;
import static ua.foodtracker.utility.Mapper.mapMealDomainToMealEntity;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealServiceImpl implements MealService {
    private static final int ITEMS_PER_PAGE = 3;
    private final MealRepository mealRepository;


    @Override
    public Page<Meal> findAllByPage(String pageNumber) {
        if (pageNumber == null) {
            return mealRepository.findAll(PageRequest.of(1, ITEMS_PER_PAGE)).map(Mapper::mapMealEntityToMealDomain);
        }
        try {
            return mealRepository.findAll(PageRequest.of(Integer.parseInt(pageNumber), ITEMS_PER_PAGE)).map(Mapper::mapMealEntityToMealDomain);
        } catch (NumberFormatException ex) {

            return mealRepository.findAll(PageRequest.of(1, ITEMS_PER_PAGE)).map(Mapper::mapMealEntityToMealDomain);
        }
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
