package ua.foodtracker.service.mapper;

import org.junit.Test;
import ua.foodtracker.domain.Gender;
import ua.foodtracker.domain.Lifestyle;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.entit.GenderEntity;
import ua.foodtracker.entit.LifestyleEntity;
import ua.foodtracker.entit.RoleEntity;
import ua.foodtracker.entit.UserEntity;
import ua.foodtracker.entit.UserGoalEntity;
import ua.foodtracker.service.mapper.impl.UserMapper;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class UserMapperTest {
    private static final UserGoalEntity USER_GOAL_ENTITY = getUserGoalEntity();
    private static final User ADMIN = getAdmin();
    private static final User USER = getUser();
    private static final UserEntity USER_ENTITY = getUserEntity();
    private static final UserEntity ADMIN_ENTITY = getAdminEntity();

    private UserMapper mapper = new UserMapper();

    @Test
    public void testMapUserToDomain() {
        User user = mapper.mapToDomain(USER_ENTITY);

        assertThat(user.getId(), is(USER_ENTITY.getId()));
        assertThat(user.getEmail(), is(USER_ENTITY.getEmail()));
        assertThat(user.getHeight(), is(USER_ENTITY.getHeight()));
        assertThat(user.getWeight(), is(USER_ENTITY.getWeight()));
        assertThat(user.getLifestyle(), is(Lifestyle.ACTIVE));
        assertThat(user.getRole(), is(Role.USER));
        assertThat(user.getPassword(), is(USER_ENTITY.getPassword()));
        assertThat(user.getBirthday(), is(USER_ENTITY.getBirthday()));
        assertThat(user.getFirstName(), is(USER_ENTITY.getFirstName()));
        assertThat(user.getLastName(), is(USER_ENTITY.getLastName()));
        assertThat(user.getUserGoal(), notNullValue());
    }

    @Test
    public void testMapAdminToDomain() {
        User user = mapper.mapToDomain(ADMIN_ENTITY);

        assertThat(user.getId(), is(ADMIN_ENTITY.getId()));
        assertThat(user.getEmail(), is(ADMIN_ENTITY.getEmail()));
        assertThat(user.getHeight(), is(ADMIN_ENTITY.getHeight()));
        assertThat(user.getWeight(), is(ADMIN_ENTITY.getWeight()));
        assertThat(user.getLifestyle(), is(Lifestyle.ACTIVE));
        assertThat(user.getRole(), is(Role.ADMIN));
        assertThat(user.getPassword(), is(ADMIN_ENTITY.getPassword()));
        assertThat(user.getBirthday(), is(ADMIN_ENTITY.getBirthday()));
        assertThat(user.getFirstName(), is(ADMIN_ENTITY.getFirstName()));
        assertThat(user.getLastName(), is(ADMIN_ENTITY.getLastName()));
        assertThat(user.getUserGoal(), notNullValue());
    }

    @Test
    public void testMapUserToEntity() {
        UserEntity user = mapper.mapToEntity(USER);

        assertThat(user.getId(), is(USER.getId()));
        assertThat(user.getEmail(), is(USER.getEmail()));
        assertThat(user.getHeight(), is(USER.getHeight()));
        assertThat(user.getWeight(), is(USER.getWeight()));
        assertThat(user.getLifestyle(), is(LifestyleEntity.SEDENTARY));
        assertThat(user.getRole(), is(RoleEntity.USER));
        assertThat(user.getPassword(), is(USER.getPassword()));
        assertThat(user.getBirthday(), is(USER.getBirthday()));
        assertThat(user.getFirstName(), is(USER.getFirstName()));
        assertThat(user.getLastName(), is(USER.getLastName()));
        assertThat(user.getUserGoal(), notNullValue());
    }

    @Test
    public void testMapAdminToEntity() {
        UserEntity user = mapper.mapToEntity(ADMIN);

        assertThat(user.getId(), is(ADMIN.getId()));
        assertThat(user.getEmail(), is(ADMIN.getEmail()));
        assertThat(user.getHeight(), is(ADMIN.getHeight()));
        assertThat(user.getWeight(), is(ADMIN.getWeight()));
        assertThat(user.getLifestyle(), is(LifestyleEntity.SEDENTARY));
        assertThat(user.getRole(), is(RoleEntity.ADMIN));
        assertThat(user.getPassword(), is(ADMIN.getPassword()));
        assertThat(user.getBirthday(), is(ADMIN.getBirthday()));
        assertThat(user.getFirstName(), is(ADMIN.getFirstName()));
        assertThat(user.getLastName(), is(ADMIN.getLastName()));
        assertThat(user.getUserGoal(), notNullValue());
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
        user.setUserGoal(USER_GOAL_ENTITY);
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

        user.setUserGoal(USER_GOAL_ENTITY);
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

    private static UserGoalEntity getUserGoalEntity() {
        UserGoalEntity entity = new UserGoalEntity();
        entity.setDailyWaterGoal(10);
        entity.setDailyProteinGoal(10);
        entity.setDailyFatGoal(10);
        entity.setDailyCarbohydrateGoal(10);
        entity.setDailyEnergyGoal(10);
        entity.setId(10);
        return entity;
    }
}
