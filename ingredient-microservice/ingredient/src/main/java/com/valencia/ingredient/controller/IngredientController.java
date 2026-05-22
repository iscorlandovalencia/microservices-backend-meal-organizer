package com.valencia.ingredient.controller;

import com.valencia.ingredient.dto.IngredientDTO;
import com.valencia.ingredient.entity.Ingredient;
import com.valencia.ingredient.service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/ingredients")
    public List<IngredientDTO> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(
            @PathVariable("id") Long ingredientId) {
        IngredientDTO ingredient = ingredientService.getIngredientById(ingredientId);
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/ingredients/ids")
    public ResponseEntity<List<IngredientDTO>> getIngredientsByIds(@RequestBody List<Long> ids) {
        List<IngredientDTO> ingredients = ingredientService.getIngredientsByIds(ids);
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping("/ingredients/list")
    public ResponseEntity<Ingredient> createIngredients(@Valid @RequestBody List<IngredientDTO> ingredientList) {
        ingredientService.createIngredients(ingredientList);
        return ResponseEntity.ok().build();
    }

    @PostMapping( value = "/ingredients", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ingredient> createIngredient(
            @Valid @RequestBody Ingredient fromIngredient) {
        return ResponseEntity.ok(ingredientService.createIngredient(fromIngredient));
    }

    @PutMapping("/ingredient/{id}")
    public ResponseEntity<Ingredient> updateIngredient(
            @PathVariable(value = "id") Long ingredientId,
            @Valid
            @RequestBody Ingredient fromIngredient) {
        final Ingredient updatedIngredient = ingredientService.updateIngredient(ingredientId, fromIngredient);
        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/ingredient/{id}")
    public Map< String, Boolean > deleteIngredient(
            @PathVariable(value = "id") Long ingredientId) {
        return ingredientService.deleteIngredient(ingredientId);
    }

}
