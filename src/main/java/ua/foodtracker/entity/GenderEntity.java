package ua.foodtracker.entity;

public enum GenderEntity {
    MALE,
    FEMALE,
    OTHER;

    public static GenderEntity

    getGenderById(Integer id) {
        if (id == null) {
            return OTHER;
        }
        switch (id) {
            case 1:
                return MALE;
            case 2:
                return FEMALE;
            default:
                return OTHER;
        }
    }

    public Integer getId() {
        return this.ordinal() + 1;
    }
}
