package com.valencia.ingredient.util;

public enum IngredientType {
    CONDIMENT("Condiment"),
    FRUIT("Fruit"),
    VEGETABLE("Vegetable"),
    MEAT("Meat"),
    GRAIN("Grain"),
    FISH("Fish"),
    BREAD("Bread"),
    SEAFOOD("Seafood"),
    SPICE("Spice"),
    BEVERAGE("Beverage"),
    HERB("Herb"),
    DAIRY("Dairy"),
    LEGUME("Legume");

    private final String type;

    IngredientType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}