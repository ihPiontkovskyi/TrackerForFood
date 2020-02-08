package ua.foodtracker.entity;

public enum LifestyleEntity {
    SEDENTARY(1.2),
    LIGHTLY_ACTIVE(1.375),
    ACTIVE(1.55),
    VERY_ACTIVE(1.725),
    NOT_SELECTED(1.0);

    private Double coefficient;

    LifestyleEntity(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Double getCoefficient() {
        return coefficient;
    }
}
