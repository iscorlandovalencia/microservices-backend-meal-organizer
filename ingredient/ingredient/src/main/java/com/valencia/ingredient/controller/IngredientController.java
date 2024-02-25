package com.valencia.ingredient.controller;

import com.valencia.ingredient.entity.Ingredient;
import com.valencia.ingredient.repository.IngredientRepository;
import com.valencia.ingredient.service.SequenceGeneratorService;
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
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/ingredients")
    public List<Ingredient> getAllEUsers() {
        return ingredientRepository.findAll();
    }

    @RequestMapping(value = "/ingredient/{id}", method = RequestMethod.GET)
    public ResponseEntity<Optional<Ingredient>> getIngredientById(@PathVariable(value = "id") Long ingredientId) throws Exception {
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));
        return ResponseEntity.ok().body(ingredient);
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Ingredient> createIngredient(@Valid @RequestBody Ingredient fromIngredient) throws Exception {

        Ingredient ingredient = new Ingredient();
        ingredient.setId(sequenceGeneratorService.generateSequence(Ingredient.SEQUENCE_NAME));
        ingredient.setName(fromIngredient.getName());
        ingredient.setType(fromIngredient.getType());
        ingredient.setImage(fromIngredient.getImage());
        ingredient.setPrice(fromIngredient.getPrice());

        return ResponseEntity.ok(ingredientRepository.save(ingredient));
    }

    @PutMapping("/ingredient/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable(value = "id") Long ingredientId, @Valid @RequestBody Ingredient fromIngredient) throws Exception {
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new Exception("User not found for this id :: " + ingredientId)));

        Ingredient thisIngredient = new Ingredient();
        if (ingredient.isPresent()) {
            thisIngredient = ingredient.get();
        }

        thisIngredient.setName(fromIngredient.getName());
        thisIngredient.setType(fromIngredient.getType());
        thisIngredient.setImage(fromIngredient.getImage());
        thisIngredient.setPrice(fromIngredient.getPrice());

        final Ingredient updatedIngredient = ingredientRepository.save(thisIngredient);
        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/ingredient/{id}")
    public Map< String, Boolean > deleteIngredient(@PathVariable(value = "id") Long ingredientId) throws Exception {
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));

        ingredientRepository.delete(ingredient.get());
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
