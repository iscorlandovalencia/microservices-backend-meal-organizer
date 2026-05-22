package com.valencia.meal.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDTO {

    private Long id;
    private String name;
    private String type;
    private Double price;
    private Double quantity;
    private String image;

}
