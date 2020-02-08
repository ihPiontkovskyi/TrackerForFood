package ua.foodtracker.entity;

public enum GenderEntity {
    MALE,
    FEMALE,
    OTHER,
    NOT_SELECTED;

    public static GenderEntity getGenderById(Integer id) {
        if (id == null) {
            return NOT_SELECTED;
        }
        switch (id) {
            case 1:
                return MALE;
            case 2:
                return FEMALE;
            case 3:
                return OTHER;
            default:
                return NOT_SELECTED;
        }
    }

    public Integer getId() {
        return this.ordinal() + 1;
    }
}
