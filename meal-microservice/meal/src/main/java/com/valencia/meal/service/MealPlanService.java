package com.valencia.meal.service;

import com.valencia.meal.dto.GeneratedMealsResponse;
import com.valencia.meal.dto.MealIngredientsDTO;
import com.valencia.meal.entity.Meal;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MealPlanService {
    private MealService mealService;

    private final Random random = new Random();
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);

    public List<GeneratedMealsResponse> generateWeeklyMeals() {
        List<GeneratedMealsResponse> weeklyMeals;
        try {
            weeklyMeals = randomMeal();
        } catch (Exception e) {
            throw new RuntimeException("Exception when generated meals", e);
        }

        return weeklyMeals;
    }

    private List<GeneratedMealsResponse> randomMeal() {
        List<Meal> allMeals = mealService.getAllMeals().stream().toList();

        // Group meals by type
        Map<String, List<Meal>> mealsByType = allMeals.stream()
                .filter(meal -> meal.getCategory() != null)
                .collect(Collectors.groupingBy(Meal::getCategory));

        // For each type, shuffle and pick up to N meals
        List<Meal> selectedMeals = new ArrayList<>();

        for (Map.Entry<String, List<Meal>> entry : mealsByType.entrySet()) {
            List<Meal> mealsOfType = new ArrayList<>(entry.getValue());
            Collections.shuffle(mealsOfType);

            // pick one or more per type — here we pick 1
            mealsOfType.stream()
                    .limit(1)
                    .forEach(selectedMeals::add);
        }

        // If you need exactly 7 meals total, shuffle again and trim
        Collections.shuffle(selectedMeals);

        // If fewer than 7, add more from existing pool
        if (selectedMeals.size() < 7) {
            List<Meal> allMealsCopy = new ArrayList<>(allMeals);
            Collections.shuffle(allMealsCopy);

            while (selectedMeals.size() < 7 && !allMealsCopy.isEmpty()) {
                Meal extra = allMealsCopy.remove(0);
                selectedMeals.add(extra);
            }
        }
        selectedMeals = selectedMeals.stream()
                .limit(7)
                .toList();

        return getMealWithIngredients(selectedMeals);
    }

    private @NotNull List<GeneratedMealsResponse> getMealWithIngredients(List<Meal> selectedMeals) {
        List<GeneratedMealsResponse> mealsWithIngredients = selectedMeals.stream()
                .map(meal -> {
                    try {
                        MealIngredientsDTO fullMeal = mealService.getMealWithIngredients(meal.getId());

                        return GeneratedMealsResponse.builder()
                                .id(fullMeal.getId())
                                .name(fullMeal.getName())
                                .category(fullMeal.getCategory())
                                .image(fullMeal.getImage())
                                .preparation(fullMeal.getPreparation())
                                .ingredients(fullMeal.getIngredients())
                                .date(null)
                                .build();

                    } catch (Exception e) {
                        throw new RuntimeException("Exception getting ingredients by ID : " + meal.getId(), e);
                    }
                })
                .toList();

        return assignDateToMeal(mealsWithIngredients);
    }

    private static @NotNull List<GeneratedMealsResponse> assignDateToMeal(List<GeneratedMealsResponse> mealsWithIngredients) {
        List<GeneratedMealsResponse> weeklyMeals = new ArrayList<>();

        // Find next Monday
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);

        for (GeneratedMealsResponse selectedMeal : mealsWithIngredients) {
            if (selectedMeal.getId() == null) {
                throw new RuntimeException("Meal ID cannot be null");
            }

            GeneratedMealsResponse dto = GeneratedMealsResponse.builder()
                    .id(selectedMeal.getId())
                    .name(selectedMeal.getName())
                    .category(selectedMeal.getCategory())
                    .image(selectedMeal.getImage())
                    .preparation(selectedMeal.getPreparation())
                    .ingredients(selectedMeal.getIngredients())
                    .date(formatter.format(calendar.getTime())) // Add dates
                    .build();

            weeklyMeals.add(dto);

            // move to next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return weeklyMeals;
    }

}
