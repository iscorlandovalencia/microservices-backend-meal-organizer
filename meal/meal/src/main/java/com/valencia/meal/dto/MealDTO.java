package com.valencia.meal.dto;

import java.util.List;

public class MealDTO {
    private Long id;
    private String category;
    private String name;
    //private List<Ingredient> ingredients;
    private List<Long> ingredients;
    private String image;
    private String preparation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Long> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

}
