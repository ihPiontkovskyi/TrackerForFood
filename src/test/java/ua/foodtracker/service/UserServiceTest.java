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
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foodtracker.domain.Gender;
import ua.foodtracker.domain.Lifestyle;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.domain.UserGoal;
import ua.foodtracker.entity.GenderEntity;
import ua.foodtracker.entity.LifestyleEntity;
import ua.foodtracker.entity.RoleEntity;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.repository.UserRepository;
import ua.foodtracker.service.impl.UserServiceImpl;
import ua.foodtracker.service.mapper.Mapper;
import ua.foodtracker.service.mapper.UserMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final User USER = getUser();
    private static final User INCORRECT_USER = getIncorrectUser();
    private static final User SAVED_USER = getSavedUser();
    private static final UserEntity USER_ENTITY = getUserEntity();

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl service;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @After
    public void reset() {
        Mockito.reset(repository, mapper);
    }

    @Test
    public void registerShouldEndSuccessfully() {
        when(repository.save(USER_ENTITY)).thenReturn(USER_ENTITY);
        when(mapper.mapToEntity(USER)).thenReturn(USER_ENTITY);
        when(repository.existsByEmail(USER.getEmail())).thenReturn(false);

        service.register(USER);

        verify(repository).save(USER_ENTITY);
        verify(repository).existsByEmail(USER.getEmail());
        verify(mapper).mapToEntity(USER);
    }

    @Test
    public void registerShouldThrowIncorrectDataException() {
        when(repository.existsByEmail(USER.getEmail())).thenReturn(true);

        exception.expect(IncorrectDataException.class);
        exception.expectMessage("user.with.email.already.exists");
        service.register(getUser());

        verify(repository).existsByEmail(USER.getEmail());
    }

    @Test
    public void registerShouldThrowIncorrectDataExceptionCase2() {
        exception.expect(IncorrectDataException.class);
        exception.expectMessage("passwords.do.not.match");
        service.register(INCORRECT_USER);
    }

    @Test
    public void modifyShouldEndSuccessfully() {
        when(repository.save(USER_ENTITY)).thenReturn(USER_ENTITY);
        when(mapper.mapToEntity(SAVED_USER)).thenReturn(USER_ENTITY);

        service.modify(SAVED_USER);

        verify(repository).save(USER_ENTITY);
        verify(mapper).mapToEntity(SAVED_USER);
    }

    @Test
    public void findByIdShouldReturnOptionalEmpty() {
        when(repository.findById(SAVED_USER.getId())).thenReturn(Optional.empty());

        Optional<User> meal = service.findById(SAVED_USER.getId().toString());

        assertThat(meal, is(Optional.empty()));
        verify(repository).findById(SAVED_USER.getId());
    }

    @Test
    public void findByIdShouldReturnOptional() {
        when(repository.findById(SAVED_USER.getId())).thenReturn(Optional.of(USER_ENTITY));
        when(mapper.mapToDomain(USER_ENTITY)).thenReturn(SAVED_USER);

        Optional<User> meal = service.findById(SAVED_USER.getId().toString());

        assertThat(meal, is(Optional.of(SAVED_USER)));
        verify(repository).findById(SAVED_USER.getId());
        verify(mapper).mapToDomain(USER_ENTITY);
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

    private static User getIncorrectUser() {
        User user = new User();
        user.setBirthday(LocalDate.now().minusYears(20));
        user.setFirstName("first-name");
        user.setEmail("email");
        user.setGender(Gender.MALE);
        user.setHeight(80);
        user.setLastName("last-name");
        user.setLifestyle(Lifestyle.SEDENTARY);
        user.setPassword("hash");
        user.setRepeatPassword("not-hash");
        user.setWeight(80);
        user.setRole(Role.USER);
        user.setUserGoal(getUserGoal());
        return user;
    }

    private static User getSavedUser() {
        User user = new User();
        user.setBirthday(LocalDate.now().minusYears(20));
        user.setFirstName("first-name");
        user.setEmail("email");
        user.setGender(Gender.MALE);
        user.setHeight(80);
        user.setLastName("last-name");
        user.setLifestyle(Lifestyle.SEDENTARY);
        user.setPassword("hash");
        user.setRepeatPassword("hash");
        user.setWeight(80);
        user.setId(1);
        user.setRole(Role.USER);
        user.setUserGoal(getUserGoal());
        return user;
    }

    private static User getUser() {
        User user = new User();
        user.setBirthday(LocalDate.now().minusYears(20));
        user.setFirstName("first-name");
        user.setEmail("email");
        user.setGender(Gender.MALE);
        user.setHeight(80);
        user.setLastName("last-name");
        user.setLifestyle(Lifestyle.SEDENTARY);
        user.setPassword("hash");
        user.setRepeatPassword("hash");
        user.setWeight(80);
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
