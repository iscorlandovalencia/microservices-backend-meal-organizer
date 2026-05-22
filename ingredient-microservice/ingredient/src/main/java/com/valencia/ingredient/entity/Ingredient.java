package com.valencia.ingredient.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document( collection = "Ingredient" )
public class Ingredient {

    @Transient
    public static final String SEQUENCE_NAME = "ingredients_sequence";

    @Id
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Indexed(unique = true)
    private String name;

    //Type is Category
    private String type;

    private Double price;

    private Double quantity;

    private String image;

}
