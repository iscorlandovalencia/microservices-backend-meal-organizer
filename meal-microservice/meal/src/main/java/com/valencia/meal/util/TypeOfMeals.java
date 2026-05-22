package com.valencia.meal.util;

public enum TypeOfMeals {
    POLLO("Pollo"),
    RES("Res"),
    MARISCOS("Mariscos"),
    PUERCO("Puerco"),
    VERDURAS("Verduras"),
    BOTANERA("Botanera"),
    MAIN_DISH("Main Dish");

    private final String displayName;

    TypeOfMeals(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
