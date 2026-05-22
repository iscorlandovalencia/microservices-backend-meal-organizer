package com.valencia.meal.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratedMealsResponse {
    private Long id;
    private String name;
    private String category;
    private String image;
    private String preparation;
    private List<IngredientDTO> ingredients;
    private String date;
}
