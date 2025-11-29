package com.valencia.ingredient.service;

import com.valencia.ingredient.dto.IngredientDTO;
import com.valencia.ingredient.entity.Ingredient;
import com.valencia.ingredient.repository.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private static final Logger LOG = LoggerFactory.getLogger(IngredientService.class);

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public List<IngredientDTO> getAllIngredients() {
        LOG.info("Get All ingredients");
        return ingredientRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public IngredientDTO getIngredientById(Long ingredientId) throws Exception {
        LOG.info("Get ingredient : " + ingredientId);
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId));
        return convertToDTO(ingredient);
    }

    public Ingredient createIngredient(Ingredient fromIngredient) throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(sequenceGeneratorService.generateSequence(Ingredient.SEQUENCE_NAME));
        ingredient.setName(fromIngredient.getName());
        ingredient.setType(fromIngredient.getType());
        ingredient.setImage(fromIngredient.getImage());
        ingredient.setQuantity(fromIngredient.getQuantity());
        ingredient.setPrice(fromIngredient.getPrice());

        LOG.info("Saving a ingredient : " + fromIngredient.getId());
        ingredientRepository.save(ingredient);
        return ingredient;
    }

    public Ingredient updateIngredient(Long ingredientId, Ingredient fromIngredient) throws Exception {
        LOG.info("Looking for Ingredient : " + ingredientId);
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));

        if (ingredient.isPresent()) {
            LOG.info("ingredient is present? : " + (ingredient.isPresent() ? "YES" : "NO"));
            fromIngredient.setId(ingredient.get().getId());
        }
        ingredientRepository.save(fromIngredient);
        LOG.info("Ingredient updated");
        return fromIngredient;
    }

    public Map<String, Boolean> deleteIngredient(Long ingredientId) throws Exception {
        LOG.info("Looking for ingredient : " + ingredientId);
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));

        Map < String, Boolean > response = new HashMap<>();
        ingredientRepository.deleteById( ingredient.isPresent() ? ingredientId : 0);
        response.put("deleted", ingredient.isPresent() ? Boolean.TRUE : Boolean.FALSE);
        LOG.info("ingredient deleted : " + ingredientId);
        return response;
    }

    private IngredientDTO convertToDTO(Ingredient ingredient) {
        IngredientDTO dto = new IngredientDTO();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setType(ingredient.getType());
        dto.setPrice(ingredient.getPrice());
        dto.setQuantity(ingredient.getQuantity());
        if (ingredient.getImage() != null) {
            dto.setImage(Base64.getEncoder().encodeToString(ingredient.getImage()));
        }

        return dto;
    }
}
