package ua.foodtracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user_goals")
public class UserGoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "daily_energy", nullable = false)
    private Integer dailyEnergyGoal;

    @Column(name = "daily_fat", nullable = false)
    private Integer dailyFatGoal;

    @Column(name = "daily_protein", nullable = false)
    private Integer dailyProteinGoal;

    @Column(name = "daily_carbohydrate", nullable = false)
    private Integer dailyCarbohydrateGoal;

    @Column(name = "daily_water", nullable = false)
    private Integer dailyWaterGoal;
}