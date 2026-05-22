package com.valencia.meal.service;

import com.valencia.meal.dto.IngredientDTO;
import com.valencia.meal.dto.MealIngredientsDTO;
import com.valencia.meal.entity.Meal;
import com.valencia.meal.repository.MealRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MealService {

    private static final Logger LOG = LoggerFactory.getLogger(MealService.class);

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private IngredientService ingredientService;

    public List<Meal> getAllMeals() {
        LOG.info("find All Meals");
        return mealRepository.findAll();
    }

    public Meal getMealById(Long mealId) {
        LOG.info("get meal by Id : {}", mealId);
        Optional<Meal> mealOpt;
        Meal theMeal;
        try {
            mealOpt = Optional.ofNullable(mealRepository.findById(mealId)
                    .orElseThrow(() -> new Exception("Meal not found for this id :: {}" + mealId)));
            if (mealOpt.isPresent()) {
                theMeal =  mealOpt.get();
            } else {
                throw new Exception("Meal not found for this id :: {}" + mealId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Meal not found for this id :: {}" + mealId, e);
        }

        return theMeal;
    }

    public Meal createMeal(Meal fromMeal){

        Meal meal = new Meal();
        meal.setId(sequenceGeneratorService.generateSequence(Meal.SEQUENCE_NAME));
        meal.setCategory(fromMeal.getCategory());
        meal.setName(fromMeal.getName());
        meal.setImage(fromMeal.getImage());
        meal.setPreparation(fromMeal.getPreparation());
        meal.setIngredients(fromMeal.getIngredients());

        LOG.info("Saving a meal : {}", meal.getId());
        return mealRepository.save(meal);
    }

    public void createMeals(@NotNull @Valid List<Meal> meals) {
        try {
            mealRepository.saveAll(meals.stream().map(meal -> {
                Meal thisMeal = new Meal();
                thisMeal.setId(sequenceGeneratorService.generateSequence(Meal.SEQUENCE_NAME));
                thisMeal.setCategory(meal.getCategory());
                thisMeal.setName(meal.getName());
                thisMeal.setImage(meal.getImage());
                thisMeal.setPreparation(meal.getPreparation());
                thisMeal.setIngredients(meal.getIngredients());
                return thisMeal;
            }).toList());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating meals", e);
        }

    }

    public Meal updateMeal(Long mealId, Meal fromMeal) {
        LOG.info("Looking for meal : {} ", mealId);
        Optional<Meal> meal;
        try {
            meal = Optional.ofNullable(mealRepository.findById(mealId)
                    .orElseThrow(() -> new Exception("Meal not found for this id :: " + mealId)));
        } catch (Exception e) {
            throw new RuntimeException("Meal not found for this id :: " + mealId, e);
        }

        Meal thisMeal = new Meal();
        if (meal.isPresent()) {
            LOG.info("Meal is present");
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

    public Map< String, Boolean > deleteMeal(Long mealId) {
        LOG.info("Looking for meal : {}", mealId);
        Optional<Meal> meal;
        try {
            meal = Optional.ofNullable(mealRepository.findById(mealId)
                    .orElseThrow(() -> new Exception("Meal not found for this id :: " + mealId)));
            if(meal.isEmpty()) {
                throw new Exception("Meal not found for this id :: " + mealId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Meal not found for this id :: " + mealId, e);
        }

        mealRepository.delete(meal.get());
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        LOG.info("Meal deleted : {}", mealId);
        return response;
    }

    public MealIngredientsDTO getMealWithIngredients(Long mealId) {
        LOG.info("Fetching meal with ingredients for ID: {}", mealId);

        // Fetch the meal
        Meal meal;
        try {
            meal = getMealById(mealId);
        } catch (Exception e) {
            throw new RuntimeException("Exception when trying to get Meal", e);
        }

        // Fetch full IngredientDTOs for the ingredient IDs
        List<IngredientDTO> ingredientDTOs = ingredientService.getIngredients(meal.getIngredients());

        // Map Meal to MealDTO
        MealIngredientsDTO mealDTO = new MealIngredientsDTO();
        mealDTO.setId(meal.getId());
        mealDTO.setCategory(meal.getCategory());
        mealDTO.setName(meal.getName());
        mealDTO.setImage(meal.getImage());
        mealDTO.setPreparation(meal.getPreparation());
        mealDTO.setIngredients(ingredientDTOs);

        return mealDTO;
    }

}
