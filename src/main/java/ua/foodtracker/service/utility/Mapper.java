package ua.foodtracker.service.utility;

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
import ua.foodtracker.entity.UserGoalEntity;

import java.time.LocalDate;
import java.time.Period;

import static org.springframework.security.crypto.bcrypt.BCrypt.gensalt;
import static org.springframework.security.crypto.bcrypt.BCrypt.hashpw;

public class Mapper {
    private Mapper() {
    }

    public static RecordEntity mapRecordDomainToRecordEntity(Record record) {
        RecordEntity entity = new RecordEntity();
        entity.setDate(record.getDate());
        entity.setId(record.getId());
        entity.setMeal(mapMealDomainToMealEntity(record.getMeal()));
        entity.setUser(mapUserDomainToUserEntity(record.getUser()));
        return entity;
    }

    public static Record mapRecordEntityToRecordDomain(RecordEntity entity) {
        return Record.builder()
                .date(entity.getDate())
                .id(entity.getId())
                .meal(mapMealEntityToMealDomain(entity.getMeal()))
                .user(mapUserEntityToUserDomain(entity.getUser()))
                .build();
    }

    public static MealEntity mapMealDomainToMealEntity(Meal meal) {
        MealEntity entity = new MealEntity();
        entity.setCarbohydrate(meal.getCarbohydrate());
        entity.setFat(meal.getFat());
        entity.setId(meal.getId());
        entity.setName(meal.getName());
        entity.setProtein(meal.getProtein());
        entity.setWater(meal.getWater());
        entity.setWeight(meal.getProtein());
        entity.setUser(mapUserDomainToUserEntity(meal.getUser()));
        return entity;
    }

    public static Meal mapMealEntityToMealDomain(MealEntity entity) {
        return Meal.builder()
                .carbohydrate(entity.getCarbohydrate())
                .fat(entity.getFat())
                .id(entity.getId())
                .name(entity.getName())
                .protein(entity.getProtein())
                .user(mapUserEntityToUserDomain(entity.getUser()))
                .water(entity.getWater())
                .weight(entity.getWeight())
                .build();
    }

    public static UserEntity mapUserDomainToUserEntity(User user) {
        if (user == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        entity.setBirthday(user.getBirthday());
        entity.setHeight(user.getHeight());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setGender(GenderEntity.valueOf(user.getGender().name()));
        entity.setLifestyle(LifestyleEntity.valueOf(user.getLifestyle().name()));
        entity.setId(user.getId());
        entity.setPassword(hashpw(user.getPassword(), gensalt()));
        entity.setRole(RoleEntity.valueOf(user.getRole().name()));
        entity.setWeight(user.getWeight());
        entity.setUserGoal(buildUserGoalEntity(user));
        return entity;
    }

    public static User mapUserEntityToUserDomain(UserEntity entity) {
        return entity == null ? null : User.builder()
                .birthday(entity.getBirthday())
                .firstName(entity.getFirstName())
                .email(entity.getFirstName())
                .gender(Gender.valueOf(entity.getGender().name()))
                .height(entity.getHeight())
                .id(entity.getId())
                .lastName(entity.getLastName())
                .lifestyle(Lifestyle.valueOf(entity.getLifestyle().name()))
                .password(entity.getPassword())
                .role(Role.valueOf(entity.getRole().name()))
                .weight(entity.getWeight())
                .userGoal(buildUserGoal(entity))
                .build();
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
