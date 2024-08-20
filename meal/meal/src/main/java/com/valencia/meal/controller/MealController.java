package com.valencia.meal.controller;

import com.valencia.meal.entity.Meal;
import com.valencia.meal.repository.MealRepository;
import com.valencia.meal.service.SequenceGeneratorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(MealController.class);

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/meals")
    public List<Meal> getAllMeals() {
        LOG.info("find All Meals");
        return mealRepository.findAll();
    }

    @GetMapping("/meal/{id}")
    public ResponseEntity<Optional<Meal>> getUserById(@PathVariable(value = "id") Long mealId) throws Exception {
        LOG.info("get meal by Id : " + mealId);
        Optional<Meal> meal = Optional.ofNullable(mealRepository.findById(mealId)
                .orElseThrow(() -> new Exception("Meal not found for this id :: " + mealId)));
        return ResponseEntity.ok().body(meal);
    }

    /*
    * Create meal example
    *  {
    "       category": "a category 3",
    "       name": "Third Meal Name",
    "       ingredients" : ["10", "20"],
    "       image": "",
    "       preparation" : "1. cortar\n2. servir"
        }
    * */
    @PostMapping("/meals")
    public ResponseEntity<Meal> createMeal(@org.jetbrains.annotations.NotNull @Valid @RequestBody Meal fromMeal) throws Exception {

        Meal meal = new Meal();
        meal.setId(sequenceGeneratorService.generateSequence(Meal.SEQUENCE_NAME));
        meal.setCategory(fromMeal.getCategory());
        meal.setName(fromMeal.getName());
        meal.setImage(fromMeal.getImage());
        meal.setPreparation(fromMeal.getPreparation());
        meal.setIngredients(fromMeal.getIngredients());

        LOG.info("Saving a meal : " + meal.getId());
        return ResponseEntity.ok(mealRepository.save(meal));
    }

    @PutMapping("/meal/{id}")
    public ResponseEntity<Meal> updateMeal(@PathVariable(value = "id") Long mealId, @Valid @RequestBody Meal fromMeal) throws Exception {
        LOG.info("Looking for meal : " + mealId);
        Optional<Meal> meal = Optional.ofNullable(mealRepository.findById(mealId)
                .orElseThrow(() -> new Exception("Meal not found for this id :: " + mealId)));

        Meal thisMeal = new Meal();
        if (meal.isPresent()) {
            LOG.info("Meal is present? : " + (meal.isPresent() ? "YES" : "NO"));
            thisMeal = meal.get();
        }

        thisMeal.setCategory(fromMeal.getCategory());
        thisMeal.setName(fromMeal.getName());
        thisMeal.setImage(fromMeal.getImage());
        thisMeal.setPreparation(fromMeal.getPreparation());
        thisMeal.setIngredients(fromMeal.getIngredients());

        final Meal updatedMeal = mealRepository.save(thisMeal);
        LOG.info("Meal updated");
        return ResponseEntity.ok(updatedMeal);
    }

    @DeleteMapping("/meal/{id}")
    public Map< String, Boolean > deleteMeal(@PathVariable(value = "id") Long mealId) throws Exception {
        LOG.info("Looking for meal : " + mealId);
        Optional<Meal> meal = Optional.ofNullable(mealRepository.findById(mealId)
                .orElseThrow(() -> new Exception("Meal not found for this id :: " + mealId)));

        mealRepository.delete(meal.get());
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        LOG.info("Meal deleted : " + mealId);
        return response;
    }
}
