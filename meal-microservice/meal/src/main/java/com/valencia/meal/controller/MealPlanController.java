package com.valencia.meal.controller;

import com.valencia.meal.dto.GeneratedMealsResponse;
import com.valencia.meal.service.MealPlanService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class MealPlanController {

    private final MealPlanService mealPlanService;

    public MealPlanController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    @GetMapping("/mealplan/week")
    public List<GeneratedMealsResponse> getWeeklyMealPlan() {
        return mealPlanService.generateWeeklyMeals();
    }
}
