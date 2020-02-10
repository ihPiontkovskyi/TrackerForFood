package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.MealEntity;
import ua.foodtracker.repository.MealRepository;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.exception.IncorrectDataException;
import ua.foodtracker.service.utility.Mapper;

import java.util.Optional;

import static ua.foodtracker.service.utility.Mapper.mapMealDomainToMealEntity;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.ServiceUtility.getNumberOfPage;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealServiceImpl implements MealService {
    private static final int ITEMS_PER_PAGE = 3;

    private final MealRepository mealRepository;

    @Override
    public Page<Meal> findAllByPage(String pageNumber) {
        if (pageNumber == null) {
            //log
            return mealRepository.findAll(PageRequest.of(0, ITEMS_PER_PAGE)).map(Mapper::mapMealEntityToMealDomain);
        }
        try {
            return mealRepository.findAll(PageRequest.of(Integer.parseInt(pageNumber), ITEMS_PER_PAGE)).map(Mapper::mapMealEntityToMealDomain);
        } catch (NumberFormatException ex) {
            //log
            return mealRepository.findAll(PageRequest.of(0, ITEMS_PER_PAGE)).map(Mapper::mapMealEntityToMealDomain);
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
    public void delete(String id, User user) {
        Optional<MealEntity> entity = findByStringParam(id, mealRepository::findById);
        if (entity.isPresent()) {
            if (entity.get().getUser() == null && user.getRole() == Role.ADMIN ||
                    entity.get().getUser() != null && entity.get().getUser().getId().equals(user.getId())) {
                mealRepository.delete(entity.get());
                return;
            } else {
                throw new IncorrectDataException("access.denied");
            }
        }
        throw new IncorrectDataException("incorrect.data");
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
