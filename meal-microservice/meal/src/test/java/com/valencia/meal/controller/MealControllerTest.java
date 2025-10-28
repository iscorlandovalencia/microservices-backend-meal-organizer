package com.valencia.meal.controller;

import com.valencia.meal.entity.Meal;
import com.valencia.meal.service.MealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MealControllerTest {

    @InjectMocks
    private MealController mealController;

    @Mock
    private MealService mealService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllMeals_ReturnsMealList() {
        // Arrange
        Meal meal1 = new Meal();
        meal1.setId(1L);
        meal1.setName("Tacos");

        Meal meal2 = new Meal();
        meal2.setId(2L);
        meal2.setName("Enchiladas");

        List<Meal> mockMeals = Arrays.asList(meal1, meal2);
        when(mealService.getAllMeals()).thenReturn(mockMeals);

        // Act
        List<Meal> result = mealController.getAllMeals();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tacos", result.get(0).getName());
        assertEquals("Enchiladas", result.get(1).getName());
    }

    @Test
    void testGetMealById_ReturnsMeal() throws Exception {
        // Arrange
        Long mealId = 1L;
        Meal mockMeal = new Meal();
        mockMeal.setId(mealId);
        mockMeal.setName("Spaghetti");

        when(mealService.getMealById(mealId)).thenReturn(mockMeal);

        // Act
        ResponseEntity<Meal> response = mealController.getMealById(mealId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mealId, response.getBody().getId());
        assertEquals("Spaghetti", response.getBody().getName());
    }

    @Test
    void testCreateMeal_ReturnsCreatedMeal() throws Exception {
        // Arrange
        Meal inputMeal = new Meal();
        inputMeal.setName("Sushi");

        Meal savedMeal = new Meal();
        savedMeal.setId(1L);
        savedMeal.setName("Sushi");

        when(mealService.createMeal(inputMeal)).thenReturn(savedMeal);

        // Act
        ResponseEntity<Meal> response = mealController.createMeal(inputMeal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Sushi", response.getBody().getName());
    }
    @Test
    void testUpdateMeal_ReturnsUpdatedMeal() throws Exception {
        // Arrange
        Long mealId = 1L;

        Meal inputMeal = new Meal();
        inputMeal.setName("Updated Pasta");

        Meal updatedMeal = new Meal();
        updatedMeal.setId(mealId);
        updatedMeal.setName("Updated Pasta");

        when(mealService.updateMeal(mealId, inputMeal)).thenReturn(updatedMeal);

        // Act
        ResponseEntity<Meal> response = mealController.updateMeal(mealId, inputMeal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mealId, response.getBody().getId());
        assertEquals("Updated Pasta", response.getBody().getName());
    }

    @Test
    void testDeleteMeal_ReturnsConfirmationMap() throws Exception {
        // Arrange
        Long mealId = 1L;
        Map<String, Boolean> expectedResponse = new HashMap<>();
        expectedResponse.put("deleted", true);

        when(mealService.deleteMeal(mealId)).thenReturn(expectedResponse);

        // Act
        Map<String, Boolean> result = mealController.deleteMeal(mealId);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("deleted"));
        assertTrue(result.get("deleted"));
    }

}