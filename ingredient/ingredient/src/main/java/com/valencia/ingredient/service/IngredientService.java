package com.valencia.ingredient.service;

import com.valencia.ingredient.entity.Ingredient;
import com.valencia.ingredient.repository.IngredientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Optional<Ingredient> getIngredientById(Long ingredientId) throws Exception {
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));
        return ingredient;
    }

    public Ingredient createIngredient(Ingredient fromIngredient) throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(sequenceGeneratorService.generateSequence(Ingredient.SEQUENCE_NAME));
        ingredient.setName(fromIngredient.getName());
        ingredient.setType(fromIngredient.getType());
        ingredient.setImage(fromIngredient.getImage());
        ingredient.setPrice(fromIngredient.getPrice());

        ingredientRepository.save(ingredient);
        return ingredient;
    }

    public Ingredient updateIngredient(Long ingredientId, Ingredient fromIngredient) throws Exception {
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));

        if (ingredient.isPresent()) {
            fromIngredient.setId(ingredient.get().getId());
        }

        ingredientRepository.save(fromIngredient);
        return fromIngredient;
    }

    public Map< String, Boolean > deleteIngredient(Long ingredientId) throws Exception {
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));

        Map < String, Boolean > response = new HashMap< >();
        if(ingredient.isPresent()){
            ingredientRepository.deleteById(ingredientId);
            response.put("deleted", Boolean.TRUE);
        }else{
            response.put("deleted", Boolean.FALSE);
        }

        return response;
    }

}
