package com.valencia.meal.service;
import com.valencia.meal.dto.GeneratedMealsResponse;
import com.valencia.meal.dto.MealIngredientsDTO;
import com.valencia.meal.entity.Meal;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MealPlanServiceGroupedTest {

    @Mock
    private MealService mealService;

    @InjectMocks
    private MealPlanService mealPlanService;

    public MealPlanServiceGroupedTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateWeeklyMeals_shouldReturn7MealsGroupedByCategory() throws Exception {
        // Arrange: create 10 meals across categories
        List<Meal> allMeals = new ArrayList<>();
        String[] categories = {"Pollo", "Res", "Mariscos", "Puerco", "Verduras", "Botanera", "Extra1", "Extra2", "Extra3", "Extra4"};

        for (long i = 1; i <= 10; i++) {
            Meal meal = new Meal();
            meal.setId(i);
            meal.setName("Meal " + i);
            meal.setCategory(categories[(int) (i - 1)]); // assign category
            allMeals.add(meal);

            // Mock getMealWithIngredients
            MealIngredientsDTO dto = new MealIngredientsDTO();
            dto.setId(i);
            dto.setName("Meal " + i);
            dto.setCategory(meal.getCategory());
            dto.setImage("meal" + i + ".jpg");
            dto.setPreparation("Preparation " + i);
            dto.setIngredients(List.of()); // empty for simplicity

            when(mealService.getMealWithIngredients(i)).thenReturn(dto);
        }

        when(mealService.getAllMeals()).thenReturn(allMeals);

        // Act
        List<GeneratedMealsResponse> weeklyMeals = mealPlanService.generateWeeklyMeals();

        // Assert
        assertNotNull(weeklyMeals);
        assertEquals(7, weeklyMeals.size(), "Should return exactly 7 meals");

        // Ensure each meal has a category and date
        weeklyMeals.forEach(meal -> {
            assertNotNull(meal.getCategory(), "Meal should have a category");
            assertNotNull(meal.getDate(), "Meal should have a date");
        });

        // Verify grouping logic: no duplicate categories in result
        long distinctCategories = weeklyMeals.stream()
                .map(GeneratedMealsResponse::getCategory)
                .distinct()
                .count();

        assertEquals(weeklyMeals.size(), distinctCategories, "Each meal should be from a different category");
    }

    @Test
    void generateWeeklyMeals_shouldReturn7MealsEvenWith6Types() throws Exception {
        // Arrange: create 6 meals across 6 categories
        List<Meal> allMeals = new ArrayList<>();
        String[] categories = {"Pollo", "Res", "Mariscos", "Puerco", "Verduras", "Botanera"};

        for (long i = 1; i <= 6; i++) {
            Meal meal = new Meal();
            meal.setId(i);
            meal.setName("Meal " + i);
            meal.setCategory(categories[(int) (i - 1)]);
            allMeals.add(meal);

            // Mock getMealWithIngredients
            MealIngredientsDTO dto = new MealIngredientsDTO();
            dto.setId(i);
            dto.setName("Meal " + i);
            dto.setCategory(meal.getCategory());
            dto.setImage("meal" + i + ".jpg");
            dto.setPreparation("Preparation " + i);
            dto.setIngredients(List.of());

            when(mealService.getMealWithIngredients(i)).thenReturn(dto);
        }

        when(mealService.getAllMeals()).thenReturn(allMeals);

        // Act
        List<GeneratedMealsResponse> weeklyMeals = mealPlanService.generateWeeklyMeals();

        // Assert
        assertNotNull(weeklyMeals);
        assertEquals(7, weeklyMeals.size(), "Should return 7 meals even if only 6 types exist");

        // Ensure each meal has a category and date
        weeklyMeals.forEach(meal -> {
            assertNotNull(meal.getCategory(), "Meal should have a category");
            assertNotNull(meal.getDate(), "Meal should have a date");
        });
    }

    @Test
    void generateWeeklyMeals_shouldReturn7MealsWithAll6CategoriesAndOneRepeat() throws Exception {
        // Arrange: create 6 meals across 6 categories
        List<Meal> allMeals = new ArrayList<>();
        String[] categories = {"Pollo", "Res", "Mariscos", "Puerco", "Verduras", "Botanera"};

        for (long i = 1; i <= 6; i++) {
            Meal meal = new Meal();
            meal.setId(i);
            meal.setName("Meal " + i);
            meal.setCategory(categories[(int) (i - 1)]);
            allMeals.add(meal);

            // Mock getMealWithIngredients
            MealIngredientsDTO dto = new MealIngredientsDTO();
            dto.setId(i);
            dto.setName("Meal " + i);
            dto.setCategory(meal.getCategory());
            dto.setImage("meal" + i + ".jpg");
            dto.setPreparation("Preparation " + i);
            dto.setIngredients(List.of());

            when(mealService.getMealWithIngredients(i)).thenReturn(dto);
        }

        when(mealService.getAllMeals()).thenReturn(allMeals);

        // Act
        List<GeneratedMealsResponse> weeklyMeals = mealPlanService.generateWeeklyMeals();

        // Assert
        assertNotNull(weeklyMeals);
        assertEquals(7, weeklyMeals.size(), "Should return 7 meals even if only 6 categories exist");

        // Ensure all 6 categories are present
        List<String> categoriesReturned = weeklyMeals.stream()
                .map(GeneratedMealsResponse::getCategory)
                .toList();

        for (String category : categories) {
            assertTrue(categoriesReturned.contains(category),
                    "Weekly meals should contain category: " + category);
        }

        // Ensure at least one category is repeated
        long distinctCategories = categoriesReturned.stream().distinct().count();
        assertEquals(6, distinctCategories, "Should contain exactly 6 distinct categories");
        assertTrue(categoriesReturned.size() > distinctCategories,
                "One category should be repeated to make 7 meals");

        // Ensure each meal has a date
        weeklyMeals.forEach(meal -> assertNotNull(meal.getDate(), "Meal should have a date"));
    }

}
