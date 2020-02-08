package ua.foodtracker.service.impl;

import ua.foodtracker.annotation.Autowired;
import ua.foodtracker.annotation.Service;
import ua.foodtracker.dao.MealDao;
import ua.foodtracker.dao.Page;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.domain.Meal;
import ua.foodtracker.service.utility.EntityMapper;
import ua.foodtracker.validator.impl.MealValidator;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.foodtracker.service.utility.EntityMapper.mapMealToEntityMeal;
import static ua.foodtracker.service.utility.ServiceUtility.addByType;
import static ua.foodtracker.service.utility.ServiceUtility.deleteByStringId;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.ServiceUtility.getNumberOfPage;
import static ua.foodtracker.service.utility.ServiceUtility.modifyByType;

@Service
public class MealServiceImpl implements MealService {
    private static final Long ITEMS_PER_PAGE = 3L;

    @Autowired
    private MealDao mealDao;

    @Autowired
    private MealValidator mealValidator;

    @Override
    public List<Meal> findAllByPage(String pageNumber) {
        return findByStringParam(pageNumber, mealValidator, number -> {
            if (number < 1 || number > pageCount()) {
                number = 1;
            }
            return mealDao.findAll(new Page(number, ITEMS_PER_PAGE)).stream()
                    .map(EntityMapper::mapEntityMealToMeal)
                    .collect(Collectors.toList());
        });
    }

    @Override
    public long pageCount() {
        return getNumberOfPage(mealDao.count(), ITEMS_PER_PAGE);
    }

    @Override
    public void add(Meal meal) {
        addByType(meal, mealValidator, obj -> mealDao.save(mapMealToEntityMeal(obj)));
    }

    @Override
    public void delete(String id) {
        deleteByStringId(id, mealValidator, intId -> mealDao.deleteById(intId));
    }

    @Override
    public void modify(Meal meal) {
        modifyByType(meal, mealValidator, obj -> mealDao.update(mapMealToEntityMeal(obj)));
    }

    @Override
    public Optional<Meal> findById(String id) {
        return findByStringParam(id, mealValidator, intId -> mealDao.findById(intId).map(EntityMapper::mapEntityMealToMeal));
    }

    @Override
    public void setLocale(Locale locale) {
        mealValidator.setLocale(locale);
    }
}
