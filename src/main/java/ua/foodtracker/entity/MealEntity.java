package ua.foodtracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "meals")
public class MealEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "protein", nullable = false)
    private Integer protein;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "carbohydrate", nullable = false)
    private Integer carbohydrate;

    @Column(name = "fat", nullable = false)
    private Integer fat;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "water", nullable = false)
    private Integer water;
}
