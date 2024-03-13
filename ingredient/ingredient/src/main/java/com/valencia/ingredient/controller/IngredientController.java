package com.valencia.ingredient.controller;

import com.valencia.ingredient.entity.Ingredient;
import com.valencia.ingredient.repository.IngredientRepository;
import com.valencia.ingredient.service.IngredientService;
import com.valencia.ingredient.service.SequenceGeneratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/ingredients")
    public List<Ingredient> getAllUsers() {
        return ingredientService.getAllIngredients();
    }

    @RequestMapping(value = "/ingredient/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable(value = "id") Long ingredientId) throws Exception {
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientService.getIngredientById(ingredientId)
                .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));
        return ResponseEntity.ok().body(ingredient.get());
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Ingredient> createIngredient(@Valid @RequestBody Ingredient fromIngredient) throws Exception {
        return ResponseEntity.ok(ingredientService.createIngredient(fromIngredient));
    }

    @PutMapping("/ingredient/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable(value = "id") Long ingredientId, @Valid @RequestBody Ingredient fromIngredient) throws Exception {
        final Ingredient updatedIngredient = ingredientService.updateIngredient(ingredientId, fromIngredient);
        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/ingredient/{id}")
    public Map< String, Boolean > deleteIngredient(@PathVariable(value = "id") Long ingredientId) throws Exception {
        return ingredientService.deleteIngredient(ingredientId);
    }

}
