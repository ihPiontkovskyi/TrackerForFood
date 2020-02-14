package ua.foodtracker.service.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import ua.foodtracker.service.mapper.impl.MealMapper;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MealMapperTest {
    private static final User ADMIN = getAdmin();
    private static final User USER = getUser();
    private static final UserEntity USER_ENTITY = getUserEntity();
    private static final UserEntity ADMIN_ENTITY = getAdminEntity();
    private static final Meal MEAL = getMeal();
    private static final Meal MEAL_WITH_USER = getMealWithUser();
    private static final Meal MEAL_WITH_ADMIN = getMealWithAdmin();
    private static final MealEntity MEAL_ENTITY = getMealEntity();
    private static final MealEntity MEAL_ENTITY_WITH_USER = getMealEntityWithUser();
    private static final MealEntity MEAL_ENTITY_WITH_ADMIN = getMealEntityWithAdmin();

    @Mock
    private Mapper<User, UserEntity> userMapper;

    @InjectMocks
    private MealMapper mapper;

    @Test
    public void testMapToDomain() {
        when(userMapper.mapToDomain(USER_ENTITY)).thenReturn(USER);

        Meal currentMeal = mapper.mapToDomain(MEAL_ENTITY_WITH_USER);

        assertThat(currentMeal.getId(), is(MEAL_ENTITY_WITH_USER.getId()));
        assertThat(currentMeal.getProtein(), is(MEAL_ENTITY_WITH_USER.getProtein()));
        assertThat(currentMeal.getCarbohydrate(), is(MEAL_ENTITY_WITH_USER.getCarbohydrate()));
        assertThat(currentMeal.getFat(), is(MEAL_ENTITY_WITH_USER.getFat()));
        assertThat(currentMeal.getWater(), is(MEAL_ENTITY_WITH_USER.getWater()));
        assertThat(currentMeal.getName(), is(MEAL_ENTITY_WITH_USER.getName()));
        assertThat(currentMeal.getWeight(), is(MEAL_ENTITY_WITH_USER.getWeight()));
        assertThat(currentMeal.getUser(), is(USER));
        verify(userMapper).mapToDomain(USER_ENTITY);
    }

    @Test
    public void testMapToDomainWithoutUser() {
        when(userMapper.mapToDomain(null)).thenReturn(null);

        Meal currentMeal = mapper.mapToDomain(MEAL_ENTITY);

        assertThat(currentMeal.getId(), is(MEAL_ENTITY_WITH_USER.getId()));
        assertThat(currentMeal.getProtein(), is(MEAL_ENTITY_WITH_USER.getProtein()));
        assertThat(currentMeal.getCarbohydrate(), is(MEAL_ENTITY_WITH_USER.getCarbohydrate()));
        assertThat(currentMeal.getFat(), is(MEAL_ENTITY_WITH_USER.getFat()));
        assertThat(currentMeal.getWater(), is(MEAL_ENTITY_WITH_USER.getWater()));
        assertThat(currentMeal.getName(), is(MEAL_ENTITY_WITH_USER.getName()));
        assertThat(currentMeal.getWeight(), is(MEAL_ENTITY_WITH_USER.getWeight()));
        assertThat(currentMeal.getUser(), nullValue());
        verify(userMapper).mapToDomain(isNull());
    }

    @Test
    public void testMapToDomainWithAdmin() {
        when(userMapper.mapToDomain(ADMIN_ENTITY)).thenReturn(null);

        Meal currentMeal = mapper.mapToDomain(MEAL_ENTITY_WITH_ADMIN);

        assertThat(currentMeal.getId(), is(MEAL_ENTITY_WITH_USER.getId()));
        assertThat(currentMeal.getProtein(), is(MEAL_ENTITY_WITH_USER.getProtein()));
        assertThat(currentMeal.getCarbohydrate(), is(MEAL_ENTITY_WITH_USER.getCarbohydrate()));
        assertThat(currentMeal.getFat(), is(MEAL_ENTITY_WITH_USER.getFat()));
        assertThat(currentMeal.getWater(), is(MEAL_ENTITY_WITH_USER.getWater()));
        assertThat(currentMeal.getName(), is(MEAL_ENTITY_WITH_USER.getName()));
        assertThat(currentMeal.getWeight(), is(MEAL_ENTITY_WITH_USER.getWeight()));
        assertThat(currentMeal.getUser(), nullValue());
        verify(userMapper).mapToDomain(ADMIN_ENTITY);
    }

    @Test
    public void testMapToEntity() {
        when(userMapper.mapToEntity(USER)).thenReturn(USER_ENTITY);

        MealEntity currentMeal = mapper.mapToEntity(MEAL_WITH_USER);

        assertThat(currentMeal.getId(), is(MEAL_WITH_USER.getId()));
        assertThat(currentMeal.getProtein(), is(MEAL_WITH_USER.getProtein()));
        assertThat(currentMeal.getCarbohydrate(), is(MEAL_WITH_USER.getCarbohydrate()));
        assertThat(currentMeal.getFat(), is(MEAL_WITH_USER.getFat()));
        assertThat(currentMeal.getWater(), is(MEAL_WITH_USER.getWater()));
        assertThat(currentMeal.getName(), is(MEAL_WITH_USER.getName()));
        assertThat(currentMeal.getWeight(), is(MEAL_WITH_USER.getWeight()));
        assertThat(currentMeal.getUser(), is(USER_ENTITY));
        verify(userMapper).mapToEntity(USER);
    }

    @Test
    public void testMapToEntityWithoutUser() {
        when(userMapper.mapToEntity(null)).thenReturn(null);

        MealEntity currentMeal = mapper.mapToEntity(MEAL);

        assertThat(currentMeal.getId(), is(MEAL_ENTITY.getId()));
        assertThat(currentMeal.getProtein(), is(MEAL_ENTITY.getProtein()));
        assertThat(currentMeal.getCarbohydrate(), is(MEAL_ENTITY.getCarbohydrate()));
        assertThat(currentMeal.getFat(), is(MEAL_ENTITY.getFat()));
        assertThat(currentMeal.getWater(), is(MEAL_ENTITY.getWater()));
        assertThat(currentMeal.getName(), is(MEAL_ENTITY.getName()));
        assertThat(currentMeal.getWeight(), is(MEAL_ENTITY.getWeight()));
        assertThat(currentMeal.getUser(), nullValue());
        verify(userMapper).mapToEntity(isNull());
    }

    @Test
    public void testMapToEntityWithAdmin() {
        when(userMapper.mapToEntity(ADMIN)).thenReturn(null);

        MealEntity currentMeal = mapper.mapToEntity(MEAL_WITH_ADMIN);

        assertThat(currentMeal.getId(), is(MEAL_WITH_ADMIN.getId()));
        assertThat(currentMeal.getProtein(), is(MEAL_WITH_ADMIN.getProtein()));
        assertThat(currentMeal.getCarbohydrate(), is(MEAL_WITH_ADMIN.getCarbohydrate()));
        assertThat(currentMeal.getFat(), is(MEAL_WITH_ADMIN.getFat()));
        assertThat(currentMeal.getWater(), is(MEAL_WITH_ADMIN.getWater()));
        assertThat(currentMeal.getName(), is(MEAL_WITH_ADMIN.getName()));
        assertThat(currentMeal.getWeight(), is(MEAL_WITH_ADMIN.getWeight()));
        assertThat(currentMeal.getUser(), nullValue());
        verify(userMapper).mapToEntity(ADMIN);
    }

    private static Meal getMeal() {
        Meal meal = new Meal();
        meal.setCarbohydrate(10);
        meal.setFat(10);
        meal.setId(1);
        meal.setName("name");
        meal.setProtein(10);
        meal.setWater(10);
        meal.setWeight(100);
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

    private static Meal getMealWithAdmin() {
        Meal meal = new Meal();
        meal.setCarbohydrate(10);
        meal.setFat(10);
        meal.setId(1);
        meal.setName("name");
        meal.setProtein(10);
        meal.setWater(10);
        meal.setWeight(10);
        meal.setUser(ADMIN);
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

    private static MealEntity getMealEntityWithAdmin() {
        MealEntity entity = new MealEntity();
        entity.setUser(getAdminEntity());
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

    private static UserEntity getAdminEntity() {
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
        user.setRole(RoleEntity.ADMIN);
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
