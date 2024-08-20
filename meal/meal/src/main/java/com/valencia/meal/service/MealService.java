package com.valencia.meal.service;

import com.valencia.meal.controller.MealController;
import com.valencia.meal.entity.Meal;
import com.valencia.meal.repository.MealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MealService {

    private static final Logger LOG = LoggerFactory.getLogger(MealController.class);

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public List<Meal> getAllMeals() {
        LOG.info("find All Meals");
        return mealRepository.findAll();
    }

    public Meal getMealById(Long mealId) throws Exception {
        LOG.info("get meal by Id : " + mealId);
        Optional<Meal> meal = Optional.ofNullable(mealRepository.findById(mealId)
                .orElseThrow(() -> new Exception("Meal not found for this id :: " + mealId)));
        return meal.get();
    }

    public Meal createMeal(Meal fromMeal) throws Exception {

        Meal meal = new Meal();
        meal.setId(sequenceGeneratorService.generateSequence(Meal.SEQUENCE_NAME));
        meal.setCategory(fromMeal.getCategory());
        meal.setName(fromMeal.getName());
        meal.setImage(fromMeal.getImage());
        meal.setPreparation(fromMeal.getPreparation());
        meal.setIngredients(fromMeal.getIngredients());

        LOG.info("Saving a meal : " + meal.getId());
        return mealRepository.save(meal);
    }

    public Meal updateMeal(Long mealId, Meal fromMeal) throws Exception {
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
        return updatedMeal;
    }

    public Map< String, Boolean > deleteMeal(Long mealId) throws Exception {
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
