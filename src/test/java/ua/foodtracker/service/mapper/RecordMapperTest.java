package ua.foodtracker.service.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.foodtracker.domain.Gender;
import ua.foodtracker.domain.Lifestyle;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.Record;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.domain.UserGoal;
import ua.foodtracker.entity.GenderEntity;
import ua.foodtracker.entity.LifestyleEntity;
import ua.foodtracker.entity.MealEntity;
import ua.foodtracker.entity.RecordEntity;
import ua.foodtracker.entity.RoleEntity;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.service.mapper.impl.RecordMapper;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecordMapperTest {
    private static final Meal MEAL = getMeal();
    private static final MealEntity MEAL_ENTITY = getMealEntity();
    private static final User USER = getUser();
    private static final UserEntity USER_ENTITY = getUserEntity();
    private static final Record RECORD = getRecord();
    private static final RecordEntity RECORD_ENTITY = getRecordEntity();

    @Mock
    private Mapper<Meal, MealEntity> mealMapper;
    @Mock
    private Mapper<User, UserEntity> userMapper;

    @InjectMocks
    private RecordMapper mapper;

    @Test
    public void mapRecordToDomain() {
        when(userMapper.mapToDomain(USER_ENTITY)).thenReturn(USER);
        when(mealMapper.mapToDomain(MEAL_ENTITY)).thenReturn(MEAL);

        Record record = mapper.mapToDomain(RECORD_ENTITY);

        assertThat(record.getId(), is(RECORD_ENTITY.getId()));
        assertThat(record.getDate(), is(RECORD_ENTITY.getDate()));
        assertThat(record.getUser(), equalTo(USER));
        assertThat(record.getMeal(), equalTo(MEAL));
        verify(userMapper).mapToDomain(USER_ENTITY);
        verify(mealMapper).mapToDomain(MEAL_ENTITY);
    }

    @Test
    public void mapRecordToEntity() {
        when(userMapper.mapToEntity(USER)).thenReturn(USER_ENTITY);
        when(mealMapper.mapToEntity(MEAL)).thenReturn(MEAL_ENTITY);

        RecordEntity record = mapper.mapToEntity(RECORD);

        assertThat(record.getId(), is(RECORD.getId()));
        assertThat(record.getDate(), is(RECORD.getDate()));
        assertThat(record.getUser(), equalTo(USER_ENTITY));
        assertThat(record.getMeal(), equalTo(MEAL_ENTITY));
        verify(userMapper).mapToEntity(USER);
        verify(mealMapper).mapToEntity(MEAL);
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

    private static User getUser() {
        User user = getUserWithoutRole();
        user.setId(1);
        user.setRole(Role.USER);
        user.setUserGoal(getUserGoal());
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

    private static Record getRecord() {
        return Record.builder()
                .date(LocalDate.now().minusWeeks(1))
                .meal(MEAL)
                .user(USER)
                .id(1)
                .build();
    }

    private static RecordEntity getRecordEntity() {
        RecordEntity entity = new RecordEntity();
        entity.setId(1);
        entity.setUser(USER_ENTITY);
        entity.setMeal(MEAL_ENTITY);
        entity.setDate(LocalDate.now().minusWeeks(1));
        return entity;
    }

    private static UserGoal getUserGoal() {
        return UserGoal.builder()
                .dailyWaterGoal(2000)
                .dailyProteinGoal(100)
                .dailyFatGoal(100)
                .dailyEnergyGoal(2100)
                .dailyCarbohydrateGoal(100)
                .id(1)
                .build();
    }
}
