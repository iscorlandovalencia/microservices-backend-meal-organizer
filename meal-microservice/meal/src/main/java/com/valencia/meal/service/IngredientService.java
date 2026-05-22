package com.valencia.meal.service;

import com.valencia.meal.dto.IngredientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class IngredientService {

    private static final Logger LOG = LoggerFactory.getLogger(IngredientService.class);

    private final RestTemplate restTemplate;

    @Value("${external-services.ingredients-url}")
    private String ingredientsUrl;

    public IngredientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<IngredientDTO> getIngredients(List<Long> ingredientIds) {
        LOG.info("Fetching ingredients from external service: {}/ids", ingredientsUrl);

        try {
            ResponseEntity<List<IngredientDTO>> response =
                    restTemplate.exchange(
                            ingredientsUrl + "/ids",
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<IngredientDTO>>() {}
                    );
            List<IngredientDTO> ingredients = response.getBody();

            LOG.info("Ingredients fetched successfully: {}", ingredients);
            if(ingredients == null){
                throw new RuntimeException("Failed to fetch ingredients from external service");
            }
            return ingredients.stream().map(ingredient -> {
                IngredientDTO ingredientDTO = new IngredientDTO();
                ingredientDTO.setId(ingredient.getId());
                ingredientDTO.setName(ingredient.getName());
                ingredientDTO.setType(ingredient.getType());
                ingredientDTO.setPrice(ingredient.getPrice());
                ingredientDTO.setQuantity(ingredient.getQuantity());
                ingredientDTO.setImage(ingredient.getImage());
                return ingredientDTO;
            }).toList();
        } catch (Exception e) {
            LOG.error("Error fetching ingredients: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch ingredients from external service");
        }
    }

}