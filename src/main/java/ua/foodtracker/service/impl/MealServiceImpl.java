package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.MealEntity;
import ua.foodtracker.exception.AccessDeniedException;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.repository.MealRepository;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.mapper.MealMapper;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.ServiceUtility.getNumberOfPage;
import static ua.foodtracker.service.utility.ServiceUtility.isAccessed;
import static ua.foodtracker.utility.ParameterParser.parsePageNumber;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealServiceImpl implements MealService {
    private static final int ITEMS_PER_PAGE = 3;
    public static final String INCORRECT_DATA = "incorrect.data";

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    @Override
    public Page<Meal> findAllByPage(String pageNumber) {
        PageRequest request = PageRequest.of(parsePageNumber(pageNumber, 0, pageCount()), ITEMS_PER_PAGE);
        return mealRepository.findAll(request)
                .map(mealMapper::mapToDomain);
    }

    @Override
    public int pageCount() {
        return getNumberOfPage(mealRepository.count(), ITEMS_PER_PAGE);
    }

    @Override
    public void add(Meal meal) {
        if (nonNull(meal.getId())) {
            throw new IncorrectDataException(INCORRECT_DATA);
        }

        mealRepository.save(mealMapper.mapToEntity(recountData(meal)));
    }

    @Override
    public void delete(String id, User user) {
        MealEntity entity = findByStringParam(id, mealRepository::findById)
                .orElseThrow(() -> new IncorrectDataException(INCORRECT_DATA));
        if (isAccessed(user, entity)) {
            mealRepository.delete(entity);
        } else {
            throw new AccessDeniedException("access.denied");
        }
    }

    @Override
    public void modify(Meal meal) {
        if (isNull(meal.getId())) {
            throw new IncorrectDataException(INCORRECT_DATA);
        }
        mealRepository.save(mealMapper.mapToEntity(meal));
    }

    @Override
    public Meal findById(String id) {
        return findByStringParam(id, mealRepository::findById)
                .map(mealMapper::mapToDomain)
                .orElseThrow(() -> new IncorrectDataException(INCORRECT_DATA));
    }

    @Override
    public List<Meal> findAllByNameStartWith(String term) {
        return mealRepository.findAllByNameIsStartingWith(term).stream()
                .map(mealMapper::mapToDomain)
                .collect(Collectors.toList());
    }

    private Meal recountData(Meal meal) {
        double coefficient = 100.0 / meal.getWeight();
        meal.setWeight(100);
        meal.setCarbohydrate((int) (meal.getCarbohydrate() * coefficient));
        meal.setFat((int) (meal.getFat() * coefficient));
        meal.setProtein((int) (meal.getProtein() * coefficient));
        meal.setWater((int) (meal.getWater() * coefficient));
        return meal;
    }
}
