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
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.repository.MealRepository;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.exception.IncorrectDataException;
import ua.foodtracker.service.mapper.impl.MealMapper;

import java.util.Optional;

import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.ServiceUtility.getNumberOfPage;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealServiceImpl implements MealService {
    private static final int ITEMS_PER_PAGE = 3;

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    @Override
    public Page<Meal> findAllByPage(String pageNumber) {
        if (pageNumber == null) {
            //log
            return findAllByIntParam(0);
        }
        try {
            return findAllByIntParam(Integer.parseInt(pageNumber));
        } catch (NumberFormatException ex) {
            //log
            return findAllByIntParam(0);
        }
    }

    @Override
    public long pageCount() {
        return getNumberOfPage(mealRepository.count(), ITEMS_PER_PAGE);
    }

    @Override
    public void add(Meal meal) {
        mealRepository.save(mealMapper.mapToEntity(meal));
    }

    @Override
    public void delete(String id, User user) {
        Optional<MealEntity> entity = findByStringParam(id, mealRepository::findById);
        if (entity.isPresent()) {
            if (isAccessed(user, entity.get())) {
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
        mealRepository.save(mealMapper.mapToEntity(meal));
    }

    @Override
    public Optional<Meal> findById(String id) {
        return findByStringParam(id, mealRepository::findById).map(mealMapper::mapToDomain);
    }

    private boolean isAccessed(User userInSession, MealEntity entity) {
        UserEntity currentUser = entity.getUser();
        boolean isNull = currentUser == null;

        return isNull && userInSession.getRole() == Role.ADMIN ||
                !isNull && currentUser.getId().equals(userInSession.getId());
    }

    private Page<Meal> findAllByIntParam(int i) {
        return mealRepository.findAll(PageRequest.of(i, ITEMS_PER_PAGE)).map(mealMapper::mapToDomain);
    }
}
