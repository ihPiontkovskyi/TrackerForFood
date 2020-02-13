package ua.foodtracker.service.mapper.impl;

import org.springframework.stereotype.Component;
import ua.foodtracker.domain.Gender;
import ua.foodtracker.domain.Lifestyle;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.domain.UserGoal;
import ua.foodtracker.entit.GenderEntity;
import ua.foodtracker.entit.LifestyleEntity;
import ua.foodtracker.entit.RoleEntity;
import ua.foodtracker.entit.UserEntity;
import ua.foodtracker.entit.UserGoalEntity;
import ua.foodtracker.service.mapper.Mapper;

import java.time.LocalDate;
import java.time.Period;

@Component
public class UserMapper implements Mapper<User, UserEntity> {

    @Override
    public User mapToDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User user = new User();
        user.setBirthday(entity.getBirthday());
        user.setFirstName(entity.getFirstName());
        user.setEmail(entity.getEmail());
        user.setGender(Gender.valueOf(entity.getGender().name()));
        user.setHeight(entity.getHeight());
        user.setId(entity.getId());
        user.setLastName(entity.getLastName());
        user.setLifestyle(Lifestyle.valueOf(entity.getLifestyle().name()));
        user.setPassword(entity.getPassword());
        user.setRole(Role.valueOf(entity.getRole().name()));
        user.setWeight(entity.getWeight());
        user.setUserGoal(buildUserGoal(entity));
        return user;
    }

    @Override
    public UserEntity mapToEntity(User domain) {
        if (domain == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setEmail(domain.getEmail());
        entity.setBirthday(domain.getBirthday());
        entity.setHeight(domain.getHeight());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setGender(GenderEntity.valueOf(domain.getGender().name()));
        entity.setLifestyle(LifestyleEntity.valueOf(domain.getLifestyle().name()));
        entity.setId(domain.getId());
        entity.setPassword(domain.getPassword());
        entity.setRole(domain.getRole() == null ? RoleEntity.USER : RoleEntity.valueOf(domain.getRole().name()));
        entity.setWeight(domain.getWeight());
        entity.setUserGoal(buildUserGoalEntity(domain));
        return entity;
    }

    private static UserGoalEntity buildUserGoalEntity(User user) {
        int dailyEnergy;
        int age = Period.between(user.getBirthday(), LocalDate.now()).getYears();
        switch (user.getGender()) {
            case MALE:
                dailyEnergy = (int) ((88.36 + 13.4 * user.getWeight() + 4.8 * user.getHeight() - 5.7 * age) * user.getLifestyle().getCoefficient());
                break;
            case FEMALE:
                dailyEnergy = (int) ((447.6 + 9.2 * user.getWeight() + 3.1 * user.getHeight() - 4.3 * age) * user.getLifestyle().getCoefficient());
                break;
            default:
                dailyEnergy = (int) ((100 + 13 * user.getWeight() + 5 * user.getHeight() - 6 * age) * user.getLifestyle().getCoefficient());
        }
        int sixthPart = dailyEnergy / 6;
        UserGoalEntity entity = new UserGoalEntity();
        entity.setDailyEnergyGoal(dailyEnergy);
        entity.setDailyCarbohydrateGoal(sixthPart);
        entity.setDailyFatGoal(sixthPart / 4);
        entity.setDailyProteinGoal(sixthPart / 9);
        entity.setDailyWaterGoal(2000);
        return entity;
    }

    private static UserGoal buildUserGoal(UserEntity entity) {
        return UserGoal.builder()
                .dailyCarbohydrateGoal(entity.getUserGoal().getDailyCarbohydrateGoal())
                .dailyEnergyGoal(entity.getUserGoal().getDailyEnergyGoal())
                .dailyFatGoal(entity.getUserGoal().getDailyFatGoal())
                .dailyProteinGoal(entity.getUserGoal().getDailyProteinGoal())
                .dailyWaterGoal(entity.getUserGoal().getDailyWaterGoal())
                .id(entity.getUserGoal().getId())
                .build();
    }
}
