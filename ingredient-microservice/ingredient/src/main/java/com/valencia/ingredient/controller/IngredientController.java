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
import java.util.Optional;

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
            @PathVariable("id") Long ingredientId) throws Exception {
        IngredientDTO ingredient = ingredientService.getIngredientById(ingredientId);
        return ResponseEntity.ok(ingredient);
    }

    @PostMapping( value = "/ingredients", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ingredient> createIngredient(
            @Valid @RequestBody Ingredient fromIngredient) throws Exception {
        return ResponseEntity.ok(ingredientService.createIngredient(fromIngredient));
    }

    @PutMapping("/ingredient/{id}")
    public ResponseEntity<Ingredient> updateIngredient(
            @PathVariable(value = "id") Long ingredientId,
            @Valid
            @RequestBody Ingredient fromIngredient) throws Exception {
        final Ingredient updatedIngredient = ingredientService.updateIngredient(ingredientId, fromIngredient);
        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/ingredient/{id}")
    public Map< String, Boolean > deleteIngredient(
            @PathVariable(value = "id") Long ingredientId) throws Exception {
        return ingredientService.deleteIngredient(ingredientId);
    }

}
