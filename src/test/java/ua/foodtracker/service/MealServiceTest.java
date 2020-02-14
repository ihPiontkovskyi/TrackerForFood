package ua.foodtracker.service;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ua.foodtracker.domain.Gender;
import ua.foodtracker.domain.Lifestyle;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.GenderEntity;
import ua.foodtracker.entity.LifestyleEntity;
import ua.foodtracker.entity.MealEntity;
import ua.foodtracker.entity.RoleEntity;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.exception.AccessDeniedException;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.repository.MealRepository;
import ua.foodtracker.service.impl.MealServiceImpl;
import ua.foodtracker.service.mapper.Mapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MealServiceTest {
    private static final Meal MEAL = getMeal();
    private static final Meal MEAL_WITH_USER = getMealWithUser();
    private static final MealEntity MEAL_ENTITY = getMealEntity();
    private static final MealEntity MEAL_ENTITY_WITH_USER = getMealEntityWithUser();
    private static final User ADMIN = getAdmin();
    private static final User USER = getUser();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private MealRepository repository;
    @Mock
    private Mapper<Meal, MealEntity> mapper;

    @InjectMocks
    private MealServiceImpl service;

    @After
    public void resetMocks() {
        reset(repository, mapper);
    }

    @Test
    public void findPageShouldReturnEmptyPage() {
        when(repository.findAll(PageRequest.of(0, 3))).thenReturn(Page.empty());
        when(repository.count()).thenReturn(10L);
        when(mapper.mapToDomain(any())).thenReturn(MEAL);

        Page<Meal> meals = service.findAllByPage("1");

        assertThat(meals, equalTo(Page.empty()));
        verify(repository).findAll(PageRequest.of(0, 3));
        verify(repository).count();
        verifyNoInteractions(mapper);
    }

    @Test
    public void findPageShouldReturnEmptyPageCase2() {
        when(repository.findAll(PageRequest.of(0, 3))).thenReturn(Page.empty());
        when(repository.count()).thenReturn(10L);

        Page<Meal> meals = service.findAllByPage("null");

        assertThat(meals, equalTo(Page.empty()));
        verify(repository).findAll(PageRequest.of(0, 3));
        verify(repository).count();
        verifyNoInteractions(mapper);
    }

    @Test
    public void findPageShouldReturnEmptyPageCase3() {
        when(repository.findAll(PageRequest.of(0, 3))).thenReturn(Page.empty());
        when(repository.count()).thenReturn(10L);

        Page<Meal> meals = service.findAllByPage(null);

        assertThat(meals, equalTo(Page.empty()));
        verify(repository).findAll(PageRequest.of(0, 3));
        verify(repository).count();
        verifyNoInteractions(mapper);
    }

    @Test
    public void pageCountShouldReturn3() {
        when(repository.count()).thenReturn(9L);

        int pageCount = service.pageCount();

        assertThat(pageCount, is(3));
    }

    @Test
    public void pageCountShouldReturn3Case2() {
        when(repository.count()).thenReturn(8L);

        int pageCount = service.pageCount();

        assertThat(pageCount, is(3));
    }

    @Test
    public void addShouldThrowIncorrectDataException() {
        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.add(MEAL);
    }

    @Test
    public void modifyShouldEndSuccessfully() {
        when(repository.save(MEAL_ENTITY)).thenReturn(MEAL_ENTITY);
        when(mapper.mapToEntity(MEAL)).thenReturn(MEAL_ENTITY);

        service.modify(MEAL);

        verify(repository).save(MEAL_ENTITY);
        verify(mapper).mapToEntity(MEAL);
    }

    @Test
    public void deleteShouldEndSuccessfully() {
        doNothing().when(repository).delete(MEAL_ENTITY);
        when(repository.findById(MEAL.getId())).thenReturn(Optional.of(MEAL_ENTITY));

        service.delete(MEAL.getId().toString(), ADMIN);

        verify(repository).findById(MEAL.getId());
        verify(repository).delete(MEAL_ENTITY);
    }

    @Test
    public void deleteShouldEndSuccessfullyCase2() {
        doNothing().when(repository).delete(MEAL_ENTITY);
        when(repository.findById(MEAL.getId())).thenReturn(Optional.of(MEAL_ENTITY_WITH_USER));

        service.delete(MEAL.getId().toString(), getUserWithAnotherId());

        verify(repository).findById(MEAL.getId());
        verify(repository).delete(MEAL_ENTITY_WITH_USER);
    }

    @Test
    public void deleteShouldThrowIncorrectDataException() {
        when(repository.findById(MEAL.getId())).thenReturn(Optional.of(MEAL_ENTITY));

        exception.expect(AccessDeniedException.class);
        exception.expectMessage("access.denied");
        service.delete(MEAL.getId().toString(), USER);

        verify(repository).findById(MEAL.getId());
    }

    @Test
    public void deleteShouldThrowIncorrectDataExceptionCase2() {
        when(repository.findById(MEAL.getId())).thenReturn(Optional.empty());

        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.delete(MEAL.getId().toString(), USER);

        verify(repository).findById(MEAL.getId());
    }

    @Test
    public void deleteShouldThrowIncorrectDataExceptionCase3() {
        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.delete("asd", USER);
    }

    @Test
    public void deleteShouldThrowIncorrectDataExceptionCase4() {
        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.delete(null, USER);
    }

    @Test
    public void deleteShouldThrowIncorrectDataExceptionCase5() {
        when(repository.findById(MEAL.getId())).thenReturn(Optional.of(MEAL_ENTITY_WITH_USER));

        exception.expect(AccessDeniedException.class);
        exception.expectMessage("access.denied");
        service.delete(MEAL_WITH_USER.getId().toString(), USER);

        verify(repository).findById(MEAL.getId());
    }

    @Test
    public void findByIdShouldReturnOptionalEmpty() {
        when(repository.findById(MEAL.getId())).thenReturn(Optional.empty());

        Meal meal = service.findById(MEAL.getId().toString());

        assertThat(meal, is(Optional.empty()));
        verify(repository).findById(MEAL.getId());
    }

    @Test
    public void findByIdShouldReturnOptional() {
        when(repository.findById(MEAL.getId())).thenReturn(Optional.of(MEAL_ENTITY));
        when(mapper.mapToDomain(MEAL_ENTITY)).thenReturn(MEAL);

        Meal meal = service.findById(MEAL.getId().toString());

        assertThat(meal, is(Optional.of(MEAL)));
        verify(repository).findById(MEAL.getId());
        verify(mapper).mapToDomain(MEAL_ENTITY);
    }

    @Test
    public void findByIdShouldThrowIncorrectDataException() {
        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.findById("a");
    }

    @Test
    public void findByIdShouldThrowIncorrectDataExceptionCase2() {
        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.findById(null);
    }

    private static Meal getMeal() {
        Meal meal = new Meal();
        meal.setCarbohydrate(10);
        meal.setFat(10);
        meal.setId(1);
        meal.setName("name");
        meal.setProtein(10);
        meal.setWater(10);
        meal.setWeight(10);
        meal.setUser(null);
        return meal;
    }

    private static Meal getMealWithUser() {
        Meal meal = new Meal();
        meal.setCarbohydrate(10);
        meal.setFat(10);
        meal.setId(1);
        meal.setName("name");
        meal.setProtein(10);
        meal.setWater(10);
        meal.setWeight(10);
        meal.setUser(USER);
        return meal;
    }

    private static MealEntity getMealEntity() {
        MealEntity entity = new MealEntity();
        entity.setUser(null);
        entity.setWeight(100);
        entity.setWater(10);
        entity.setId(1);
        entity.setProtein(10);
        entity.setName("name");
        entity.setFat(10);
        entity.setCarbohydrate(10);
        return entity;
    }

    private static MealEntity getMealEntityWithUser() {
        MealEntity entity = new MealEntity();
        entity.setUser(getUserEntity());
        entity.setWeight(100);
        entity.setWater(10);
        entity.setId(1);
        entity.setProtein(10);
        entity.setName("name");
        entity.setFat(10);
        entity.setCarbohydrate(10);
        return entity;
    }

    private static User getAdmin() {
        User user = getUserWithoutRole();
        user.setId(2);
        user.setRole(Role.ADMIN);
        return user;
    }

    private static User getUserWithAnotherId() {
        User user = getUserWithoutRole();
        user.setId(1);
        user.setRole(Role.USER);
        return user;
    }

    private static User getUser() {
        User user = getUserWithoutRole();
        user.setId(2);
        user.setRole(Role.USER);
        return user;
    }

    private static UserEntity getUserEntity() {
        UserEntity user = new UserEntity();
        user.setBirthday(LocalDate.now().minusYears(20));
        user.setFirstName("first-name");
        user.setEmail("email");
        user.setGender(GenderEntity.OTHER);
        user.setHeight(80);
        user.setId(1);

        user.setLastName("last-name");
        user.setLifestyle(LifestyleEntity.ACTIVE);
        user.setPassword("hash");
        user.setWeight(80);
        user.setRole(RoleEntity.USER);
        return user;
    }

    private static User getUserWithoutRole() {
        User user = new User();
        user.setBirthday(LocalDate.now().minusYears(20));
        user.setFirstName("first-name");
        user.setEmail("email");
        user.setGender(Gender.MALE);
        user.setHeight(80);
        user.setLastName("last-name");
        user.setLifestyle(Lifestyle.SEDENTARY);
        user.setPassword("hash");
        user.setWeight(80);
        return user;
    }
}
