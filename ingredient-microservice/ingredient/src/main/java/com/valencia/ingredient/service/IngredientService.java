package com.valencia.ingredient.service;

import com.valencia.ingredient.dto.IngredientDTO;
import com.valencia.ingredient.entity.Ingredient;
import com.valencia.ingredient.exception.IngredientNotFoundException;
import com.valencia.ingredient.repository.IngredientRepository;
import jakarta.validation.Valid;
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

    public List<IngredientDTO> getIngredientsByIds(List<Long> ids) {
        LOG.info("Fetching ingredients for IDs: {}", ids);
        return ingredientRepository.findAllById(ids)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public IngredientDTO getIngredientById(Long ingredientId) {
        LOG.info("Get ingredient : {}", ingredientId);
        Ingredient ingredient;
        try {
            ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId));
        } catch (Exception e) {
            throw new RuntimeException("Ingredient not found for this id :: " + ingredientId, e);
        }
        return convertToDTO(ingredient);
    }

    public IngredientDTO getIngredientByName(String name) {
        Ingredient ingredient = ingredientRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new IngredientNotFoundException("Ingrediente no encontrado: " + name));

        IngredientDTO dto = new IngredientDTO();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setType(ingredient.getType());
        dto.setPrice(ingredient.getPrice());
        dto.setQuantity(ingredient.getQuantity());
        dto.setImage(ingredient.getImage());
        return dto;
    }

    public Ingredient createIngredient(Ingredient fromIngredient){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(sequenceGeneratorService.generateSequence(Ingredient.SEQUENCE_NAME));
        ingredient.setName(fromIngredient.getName());
        ingredient.setType(fromIngredient.getType());
        ingredient.setImage(fromIngredient.getImage());
        ingredient.setQuantity(fromIngredient.getQuantity());
        ingredient.setPrice(fromIngredient.getPrice());

        LOG.info("Saving a ingredient : {}", fromIngredient.getId());
        ingredientRepository.save(ingredient);
        return ingredient;
    }

    public void createIngredients(@Valid List<IngredientDTO> ingredientList) {
        try{
            LOG.info("Creating ingredient ");
            ingredientRepository.saveAll(ingredientList.stream().map(ingredientDTO -> {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(sequenceGeneratorService.generateSequence(Ingredient.SEQUENCE_NAME));
                ingredient.setName(ingredientDTO.getName());
                ingredient.setType(ingredientDTO.getType());
                ingredient.setPrice(ingredientDTO.getPrice());
                ingredient.setQuantity(ingredientDTO.getQuantity());
                ingredient.setImage(ingredientDTO.getImage());
                return ingredient;
            }).toList());
            LOG.info("Ingredients created successfully");
        }catch (Exception exception){
            throw new RuntimeException("Error occurred while creating ingredients", exception);
        }
    }

    public Ingredient updateIngredient(Long ingredientId, Ingredient fromIngredient) {
        LOG.info("Looking for Ingredient : {}", ingredientId);
        Optional<Ingredient> ingredient;
        try {
            ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while updating ingredient", e);
        }

        if (ingredient.isPresent()) {
            LOG.info("ingredient is present? : {}", "YES");
            fromIngredient.setId(ingredient.get().getId());
        }
        ingredientRepository.save(fromIngredient);
        LOG.info("Ingredient {} updated", fromIngredient.getName());
        return fromIngredient;
    }

    public Map<String, Boolean> deleteIngredient(Long ingredientId) {
        LOG.info("Looking for ingredient : {}", ingredientId);
        Optional<Ingredient> ingredient;
        try {
            ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new Exception("Ingredient not found for this id :: " + ingredientId)));
        } catch (Exception e) {
            throw new RuntimeException("Ingredient not found for this id :: " + ingredientId, e);
        }

        Map < String, Boolean > response = new HashMap<>();
        ingredientRepository.deleteById( ingredient.isPresent() ? ingredientId : 0);
        response.put("deleted", ingredient.isPresent() ? Boolean.TRUE : Boolean.FALSE);
        LOG.info("ingredient deleted : {}", ingredientId);
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
            dto.setImage(ingredient.getImage());
        }

        return dto;
    }
}
