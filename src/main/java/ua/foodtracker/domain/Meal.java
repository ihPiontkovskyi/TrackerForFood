package ua.foodtracker.service.domain;

public class Meal {
    private Integer id;
    private String name;
    private Integer protein;
    private User user;
    private Integer carbohydrate;
    private Integer fat;
    private Integer weight;
    private Integer water;

    public Meal(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.protein = builder.protein;
        this.user = builder.user;
        this.carbohydrate = builder.carbohydrates;
        this.fat = builder.fat;
        this.weight = builder.weight;
        this.water = builder.water;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getProtein() {
        return protein;
    }

    public User getUser() {
        return user;
    }

    public Integer getCarbohydrate() {
        return carbohydrate;
    }

    public Integer getFat() {
        return fat;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getWater() {
        return water;
    }

    public Integer calculateEnergy() {
        return carbohydrate * 4 + protein * 4 + fat * 9;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Integer protein;
        private User user;
        private Integer carbohydrates;
        private Integer fat;
        private Integer weight;
        private Integer water;

        private Builder() {
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withProtein(Integer protein) {
            this.protein = protein;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withCarbohydrates(Integer carbohydrates) {
            this.carbohydrates = carbohydrates;
            return this;
        }

        public Builder withFat(Integer fat) {
            this.fat = fat;
            return this;
        }

        public Builder withWeight(Integer weight) {
            this.weight = weight;
            return this;
        }

        public Builder withWater(Integer water) {
            this.water = water;
            return this;
        }

        public Meal build() {
            return new Meal(this);
        }
    }
}
