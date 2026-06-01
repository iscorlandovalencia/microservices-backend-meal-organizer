package com.valencia.meal.util;

public enum TypeOfMeals {
    POLLO("Pollo"),
    RES("Res"),
    MARISCOS("Mariscos"),
    PUERCO("Puerco"),
    VERDURAS("Verduras");

    private final String displayName;

    TypeOfMeals(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
