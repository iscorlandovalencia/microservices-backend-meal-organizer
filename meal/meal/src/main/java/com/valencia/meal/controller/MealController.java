package com.valencia.meal.controller;

import com.valencia.meal.entity.Meal;
import com.valencia.meal.repository.MealRepository;
import com.valencia.meal.service.SequenceGeneratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class MealController {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/meals")
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    @GetMapping("/meal/{id}")
    public ResponseEntity<Optional<Meal>> getUserById(@PathVariable(value = "id") Long mealId) throws Exception {
        Optional<Meal> meal = Optional.ofNullable(mealRepository.findById(mealId)
                .orElseThrow(() -> new Exception("Meal not found for this id :: " + mealId)));
        return ResponseEntity.ok().body(meal);
    }

    /*
    * Create meal example
    *  {
    "category": "a category",
    "name": "Meal Name",
    "ingredient" : [{
        "id": "1",
        "name": "cebolla",
        "type": "verdura",
        "price": "10.0",
        "image": ""
    },{
        "id": "2",
        "name": "jitomate",
        "type": "verdura",
        "price": "12.0",
        "image": ""
    }],
    "image": "",
    "preparation" : "1. cortar\n 2. servir"
    * */
    @PostMapping("/meals")
    public ResponseEntity<Meal> createMeal(@Valid @RequestBody Meal fromMeal) throws Exception {

        Meal meal = new Meal();
        meal.setId(sequenceGeneratorService.generateSequence(Meal.SEQUENCE_NAME));
        meal.setCategory(fromMeal.getCategory());
        meal.setName(fromMeal.getName());
        meal.setImage(fromMeal.getImage());
        meal.setPreparation(fromMeal.getPreparation());
        meal.setIngredients(fromMeal.getIngredients());

        return ResponseEntity.ok(mealRepository.save(meal));
    }

    @PutMapping("/meal/{id}")
    public ResponseEntity<Meal> updateMeal(@PathVariable(value = "id") Long mealId, @Valid @RequestBody Meal fromMeal) throws Exception {
        Optional<Meal> meal = Optional.ofNullable(mealRepository.findById(mealId)
                .orElseThrow(() -> new Exception("Meal not found for this id :: " + mealId)));

        Meal thisMeal = new Meal();
        if (meal.isPresent()) {
            thisMeal = meal.get();
        }

        thisMeal.setCategory(fromMeal.getCategory());
        thisMeal.setName(fromMeal.getName());
        thisMeal.setImage(fromMeal.getImage());
        thisMeal.setPreparation(fromMeal.getPreparation());
        thisMeal.setIngredients(fromMeal.getIngredients());

        final Meal updatedMeal = mealRepository.save(thisMeal);
        return ResponseEntity.ok(updatedMeal);
    }

    @DeleteMapping("/meal/{id}")
    public Map< String, Boolean > deleteMeal(@PathVariable(value = "id") Long mealId) throws Exception {
        Optional<Meal> meal = Optional.ofNullable(mealRepository.findById(mealId)
                .orElseThrow(() -> new Exception("Meal not found for this id :: " + mealId)));

        mealRepository.delete(meal.get());
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
