package com.valencia.meal.converter;

import com.valencia.meal.dto.MealDTO;
import com.valencia.meal.entity.Meal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MealConverterTest {
    private final MealConverter mealMapper = new MealConverter();
    private MealDTO dto = new MealDTO();
    private Meal meal = new Meal();
    private List<Long> ingredientList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        ingredientList.add(1L);
        ingredientList.add(2L);
        ingredientList.add(3L);

        dto.setId(1L);
        dto.setName("Pizza");
        dto.setCategory("Italian");
        dto.setIngredients(ingredientList);
        dto.setPreparation("Bake at 220°C for 15 minutes");
        dto.setImage("pizza.jpg");

        meal.setId(1L);
        meal.setName("Lasagna");
        meal.setImage("lasagna.jpg");
        meal.setCategory("Italian");
        meal.setIngredients(ingredientList);
        meal.setPreparation("Bake for 45 minutes");
    }

    @Test
    void testToEntity_MapsAllFieldsCorrectly() {
        Meal meal = mealMapper.toEntity(dto);
        assertNotNull(meal);
        assertEquals(dto.getId(), meal.getId());
        assertEquals(dto.getName(), meal.getName());
        assertEquals(dto.getCategory(), meal.getCategory());
        assertEquals(dto.getIngredients(), meal.getIngredients());
        assertEquals(dto.getPreparation(), meal.getPreparation());
        assertEquals(dto.getImage(), meal.getImage());
    }
    @Test
    void testToDTO_MapsAllFieldsCorrectly() {
        MealDTO dto = mealMapper.toDTO(meal);
        assertNotNull(dto);
        assertEquals(meal.getId(), dto.getId());
        assertEquals(meal.getName(), dto.getName());
        assertEquals(meal.getImage(), dto.getImage());
        assertEquals(meal.getCategory(), dto.getCategory());
        assertEquals(meal.getIngredients(), dto.getIngredients());
        assertEquals(meal.getPreparation(), dto.getPreparation());
    }

}