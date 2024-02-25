package com.valencia.meal.converter;

import com.valencia.meal.dto.MealDTO;
import com.valencia.meal.entity.Meal;

public class MealConverter {

    public Meal toEntity(MealDTO dto) {
        Meal meal = new Meal();
        meal.setId(dto.getId());
        meal.setName(dto.getName());
        meal.setCategory(dto.getCategory());
        meal.setIngredients(dto.getIngredients());
        meal.setPreparation(dto.getPreparation());
        meal.setImage(dto.getImage());

        return meal;
    }

    public MealDTO toDTO(Meal entity) {
        MealDTO dto = new MealDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setImage(entity.getImage());
        dto.setCategory(entity.getCategory());
        dto.setIngredients(entity.getIngredients());
        dto.setPreparation(entity.getPreparation());

        return dto;
    }

}
