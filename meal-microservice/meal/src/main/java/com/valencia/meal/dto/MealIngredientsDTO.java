package com.valencia.meal.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealIngredientsDTO {
    private Long id;
    private String category;
    private String name;
    private List<IngredientDTO> ingredients;
    private String image;
    private String preparation;

}
