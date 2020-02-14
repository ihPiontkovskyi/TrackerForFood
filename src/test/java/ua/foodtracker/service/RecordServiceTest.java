package ua.foodtracker.service;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ua.foodtracker.domain.DailySums;
import ua.foodtracker.domain.Gender;
import ua.foodtracker.domain.HomeModel;
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
import ua.foodtracker.exception.AccessDeniedException;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.repository.RecordRepository;
import ua.foodtracker.service.impl.RecordServiceImpl;
import ua.foodtracker.service.mapper.Mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceTest {

    private static final Record RECORD = getRecord();
    private static final RecordEntity RECORD_ENTITY = getRecordEntity();

    @Mock
    private RecordRepository repository;
    @Mock
    private Mapper<Record, RecordEntity> mapper;

    @InjectMocks
    private RecordServiceImpl service;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @After
    public void reset() {
        Mockito.reset(repository, mapper);
    }

    @Test
    public void addShouldThrowIncorrectDataException() {
        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.add(RECORD);
    }

    @Test
    public void modifyShouldEndSuccessfully() {
        when(repository.save(RECORD_ENTITY)).thenReturn(RECORD_ENTITY);
        when(mapper.mapToEntity(RECORD)).thenReturn(RECORD_ENTITY);

        service.modify(RECORD);

        verify(repository).save(RECORD_ENTITY);
        verify(mapper).mapToEntity(RECORD);
    }

    @Test
    public void deleteShouldEndSuccessfully() {
        doNothing().when(repository).delete(RECORD_ENTITY);
        when(repository.findById(RECORD.getId())).thenReturn(Optional.of(RECORD_ENTITY));

        service.delete(RECORD.getId().toString(), getUser());

        verify(repository).findById(RECORD.getId());
        verify(repository).delete(RECORD_ENTITY);
    }

    @Test
    public void deleteShouldThrowIncorrectDataException() {
        when(repository.findById(RECORD.getId())).thenReturn(Optional.of(RECORD_ENTITY));

        exception.expect(AccessDeniedException.class);
        exception.expectMessage("access.denied");
        service.delete(RECORD.getId().toString(), getUserWithAnotherId());

        verify(repository).findById(RECORD.getId());
    }

    @Test
    public void deleteShouldThrowIncorrectDataExceptionCase2() {
        when(repository.findById(RECORD.getId())).thenReturn(Optional.empty());

        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.delete(RECORD.getId().toString(), getUser());

        verify(repository).findById(RECORD.getId());
    }

    @Test
    public void deleteShouldThrowIncorrectDataExceptionCase3() {
        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.delete("asd", getUser());
    }

    @Test
    public void deleteShouldThrowIncorrectDataExceptionCase4() {
        exception.expect(IncorrectDataException.class);
        exception.expectMessage("incorrect.data");
        service.delete(null, getUser());
    }

    @Test
    public void findByIdShouldReturnOptionalEmpty() {
        when(repository.findById(RECORD.getId())).thenReturn(Optional.empty());

        Optional<Record> meal = service.findById(RECORD.getId().toString());

        assertThat(meal, is(Optional.empty()));
        verify(repository).findById(RECORD.getId());
    }

    @Test
    public void findByIdShouldReturnOptional() {
        when(repository.findById(RECORD.getId())).thenReturn(Optional.of(RECORD_ENTITY));
        when(mapper.mapToDomain(RECORD_ENTITY)).thenReturn(RECORD);

        Optional<Record> meal = service.findById(RECORD.getId().toString());

        assertThat(meal, is(Optional.of(RECORD)));
        verify(repository).findById(RECORD.getId());
        verify(mapper).mapToDomain(RECORD_ENTITY);
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

    @Test
    public void calculateDailySumsTest() {
        when(repository.findAllByUserIdAndDate(getUser().getId(), LocalDate.now())).thenReturn(Collections.emptyList());

        DailySums expected = DailySums.builder()
                .sumCarbohydrates(0)
                .sumEnergy(0)
                .sumFat(0)
                .sumProtein(0)
                .sumWater(0)
                .build();
        DailySums actual = service.calculateDailySums(getUser(), LocalDate.now().toString());

        assertThat(actual, is(expected));
        verify(repository).findAllByUserIdAndDate(getUser().getId(), LocalDate.now());
    }

    @Test
    public void calculateDailySumsTestCase2() {
        when(repository.findAllByUserIdAndDate(getUser().getId(), LocalDate.now())).thenReturn(Collections.singletonList(RECORD_ENTITY));
        when(mapper.mapToDomain(RECORD_ENTITY)).thenReturn(RECORD);
        DailySums expected = DailySums.builder()
                .sumCarbohydrates(10)
                .sumEnergy(170)
                .sumFat(10)
                .sumProtein(10)
                .sumWater(10)
                .build();
        DailySums actual = service.calculateDailySums(getUser(), LocalDate.now().toString());

        assertThat(actual, is(expected));
        verify(repository).findAllByUserIdAndDate(getUser().getId(), LocalDate.now());
    }

    @Test
    public void findAllByUserIdAndDateShouldReturnCollectionEmpty() {
        when(repository.findAllByUserIdAndDate(getUser().getId(), LocalDate.now())).thenReturn(Collections.emptyList());

        List<Record> recordList = service.getRecordsByDate(getUser(), LocalDate.now().toString());

        assertThat(recordList, is(Collections.emptyList()));
        verify(repository).findAllByUserIdAndDate(getUser().getId(), LocalDate.now());
    }

    @Test
    public void findAllByUserIdAndDateShouldReturnCollectionEmptyCase2() {
        when(repository.findAllByUserIdAndDate(getUser().getId(), LocalDate.now())).thenReturn(Collections.emptyList());

        List<Record> recordList = service.getRecordsByDate(getUser(), null);

        assertThat(recordList, is(Collections.emptyList()));
        verify(repository).findAllByUserIdAndDate(getUser().getId(), LocalDate.now());
    }

    @Test
    public void findAllByUserIdAndDateShouldReturnCollectionEmptyCase3() {
        when(repository.findAllByUserIdAndDate(getUser().getId(), LocalDate.now())).thenReturn(Collections.emptyList());

        List<Record> recordList = service.getRecordsByDate(getUser(), "a");

        assertThat(recordList, is(Collections.emptyList()));
        verify(repository).findAllByUserIdAndDate(getUser().getId(), LocalDate.now());
    }

    @Test
    public void getHomeModelTest() {
        when(repository.findAllByUserIdAndDate(anyInt(), any())).thenReturn(Collections.emptyList());
        when(repository.findAllByUserIdAndDate(getUser().getId(), LocalDate.now())).thenReturn(Collections.singletonList(RECORD_ENTITY));
        when(mapper.mapToDomain(RECORD_ENTITY)).thenReturn(RECORD);

        HomeModel model = service.getHomeModel(getUser());

        DailySums expected = DailySums.builder()
                .sumCarbohydrates(10)
                .sumEnergy(170)
                .sumFat(10)
                .sumProtein(10)
                .sumWater(10)
                .build();
        assertThat(model.getDailySums(), is(expected));
        assertThat(model.getLabels(), hasItem(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM"))));
        assertThat(model.getWeeklyCarbohydrateStat(), hasItems(0, 10));
        assertThat(model.getWeeklyEnergyStat(), hasItems(0, 170));
        assertThat(model.getWeeklyFatStat(), hasItems(0, 10));
        assertThat(model.getWeeklyProteinStat(), hasItems(0, 10));
        assertThat(model.getWeeklyWaterStat(), hasItems(0, 10));
        verify(repository, times(9)).findAllByUserIdAndDate(anyInt(), any());
        verify(mapper, times(2)).mapToDomain(RECORD_ENTITY);
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

    private static User getUserWithAnotherId() {
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

    private static Record getRecord() {
        return Record.builder()
                .date(LocalDate.now().minusWeeks(1))
                .meal(getMeal())
                .user(getUser())
                .id(1)
                .build();
    }

    private static RecordEntity getRecordEntity() {
        RecordEntity entity = new RecordEntity();
        entity.setId(1);
        entity.setUser(getUserEntity());
        entity.setMeal(getMealEntity());
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
