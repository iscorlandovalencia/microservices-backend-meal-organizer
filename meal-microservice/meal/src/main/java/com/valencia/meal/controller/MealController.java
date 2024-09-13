package com.valencia.meal.controller;

import com.valencia.meal.entity.Meal;
import com.valencia.meal.service.MealService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class MealController {

    @Autowired
    private MealService mealService;

    @GetMapping("/meals")
    public List<Meal> getAllMeals() {
        return mealService.getAllMeals();
    }

    @GetMapping("/meal/{id}")
    public ResponseEntity<Meal> getMealById(
            @PathVariable(value = "id") Long mealId) throws Exception {
        return ResponseEntity.ok().body(mealService.getMealById(mealId));
    }
    @PostMapping("/meals")
    public ResponseEntity<Meal> createMeal(
            @NotNull
            @Valid
            @RequestBody Meal fromMeal) throws Exception {
        return ResponseEntity.ok(mealService.createMeal(fromMeal));
    }

    @PutMapping("/meal/{id}")
    public ResponseEntity<Meal> updateMeal(
            @PathVariable(value = "id") Long mealId,
            @Valid
            @RequestBody Meal fromMeal) throws Exception {
        return ResponseEntity.ok(mealService.updateMeal(mealId, fromMeal));
    }

    @DeleteMapping("/meal/{id}")
    public Map< String, Boolean > deleteMeal(
            @PathVariable(value = "id") Long mealId) throws Exception {
        return mealService.deleteMeal(mealId);
    }
}
