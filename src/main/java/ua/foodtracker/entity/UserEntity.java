package ua.foodtracker.entity;

import java.sql.Date;

public class UserEntity {
    private final Integer id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Integer weight;
    private final Integer height;
    private final Lifestyle lifestyle;
    private final Date birthday;
    private final Gender gender;
    private final UserGoalEntity userGoalEntity;
    private final Role role;

    private UserEntity(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.weight = builder.weight;
        this.height = builder.height;
        this.lifestyle = builder.lifestyle;
        this.birthday = builder.birthday;
        this.gender = builder.gender;
        this.role = builder.role;
        this.userGoalEntity = builder.userGoalEntity;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getHeight() {
        return height;
    }

    public Lifestyle getLifestyle() {
        return lifestyle;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public UserGoalEntity getUserGoalEntity() {
        return userGoalEntity;
    }

    public Role getRole() {
        return role;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private Integer weight;
        private Integer height;
        private Lifestyle lifestyle;
        private Date birthday;
        private Gender gender;
        private UserGoalEntity userGoalEntity;
        private Role role;

        private Builder() {
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withWeight(Integer weight) {
            this.weight = weight;
            return this;
        }

        public Builder withHeight(Integer height) {
            this.height = height;
            return this;
        }

        public Builder withLifestyle(Lifestyle lifestyle) {
            this.lifestyle = lifestyle;
            return this;
        }

        public Builder withBirthday(Date birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder withGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder withUserGoal(UserGoalEntity userGoalEntity) {
            this.userGoalEntity = userGoalEntity;
            return this;
        }

        public Builder withRole(Role role) {
            this.role = role;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }
}
