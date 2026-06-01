package com.valencia.ingredient.util;

public enum IngredientType {
    CONDIMENTO("Condimento"),
    FRUTA("Fruta"),
    VEGETAL("Vegetal"),
    CARNE("Carne"),
    GRANO("Grano"),
    PESCADO("Pescado"),
    PAN("Pan"),
    MARISCO("Marisco"),
    ESPECIA("Especia"),
    BEBIDA("Bebida"),
    HIERBA("Hierba"),
    LACTEO("Lacteo"),
    PASTA("Pasta"),
    LEGUMINOSA("Leguminosa");

    private final String type;

    IngredientType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}