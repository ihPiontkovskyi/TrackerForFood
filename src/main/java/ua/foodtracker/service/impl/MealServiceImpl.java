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
import ua.foodtracker.exception.AccessDeniedException;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.repository.MealRepository;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.mapper.Mapper;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.ServiceUtility.getNumberOfPage;
import static ua.foodtracker.utility.ParameterParser.parsePageNumber;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealServiceImpl implements MealService {
    private static final int ITEMS_PER_PAGE = 3;

    private final MealRepository mealRepository;
    private final Mapper<Meal, MealEntity> mealMapper;

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
        mealRepository.save(mealMapper.mapToEntity(meal));
    }

    @Override
    public void delete(String id, User user) {
        MealEntity entity = findByStringParam(id, mealRepository::findById)
                .orElseThrow(() -> new IncorrectDataException("incorrect.data"));
        if (isAccessed(user, entity)) {
            mealRepository.delete(entity);
        } else {
            throw new AccessDeniedException("access.denied");
        }
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

        return isNull(currentUser) && userInSession.getRole() == Role.ADMIN ||
                nonNull(currentUser) && currentUser.getId().equals(userInSession.getId());
    }
}
