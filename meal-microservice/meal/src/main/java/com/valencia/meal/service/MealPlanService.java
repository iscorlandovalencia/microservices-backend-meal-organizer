package com.valencia.meal.service;

import com.valencia.meal.dto.GeneratedMealsResponse;
import com.valencia.meal.dto.MealIngredientsDTO;
import com.valencia.meal.entity.Meal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MealPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(MealPlanService.class);

    @Autowired
    private MealService mealService;

    private final Map<Long, Integer> selectionCount = new HashMap<>();

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
        LOG.info("Generating random meals");
        List<Meal> allMeals = mealService.getAllMeals().stream().toList();

        // Agrupar por categoría
        Map<String, List<Meal>> mealsByType = allMeals.stream()
                .filter(meal -> meal.getCategory() != null)
                .collect(Collectors.groupingBy(Meal::getCategory));

        List<Meal> selectedMeals = new ArrayList<>();

        // Seleccionar uno por categoría, priorizando los menos seleccionados
        for (Map.Entry<String, List<Meal>> entry : mealsByType.entrySet()) {
            List<Meal> mealsOfType = new ArrayList<>(entry.getValue());
            Collections.shuffle(mealsOfType);

            mealsOfType.stream()
                    .min(Comparator.comparingInt(Meal::getTimesSelected))
                    .ifPresent(selectedMeals::add);
        }

        // Si faltan para llegar a 7, rellenar con los menos seleccionados del pool completo
        while (selectedMeals.size() < 7 && !allMeals.isEmpty()) {
            Meal extra = allMeals.stream()
                    .filter(meal -> !selectedMeals.contains(meal))
                    .min(Comparator.comparingInt(Meal::getTimesSelected))
                    .orElse(null);

            if (extra == null) break;
            selectedMeals.add(extra);
        }

        // Actualizar contador en BD
        selectedMeals.forEach(meal -> {
            meal.setTimesSelected(meal.getTimesSelected() + 1);
            mealService.updateMeal(meal.getId(), meal);
        });

        List<GeneratedMealsResponse> mealWithIngredients = getMealWithIngredients(selectedMeals);
        LOG.info("Generated meals with ingredients: {}", mealWithIngredients);

        return mealWithIngredients;
    }


    private @NotNull List<GeneratedMealsResponse> getMealWithIngredients(List<Meal> selectedMeals) {
        LOG.info("Fetching meal ingredients for selected meals");
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
                                .timesSelected(meal.getTimesSelected())
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
                    .date(formatter.format(calendar.getTime()))
                    .timesSelected(selectedMeal.getTimesSelected())
                    .build();

            weeklyMeals.add(dto);

            // move to next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return weeklyMeals;
    }

}
