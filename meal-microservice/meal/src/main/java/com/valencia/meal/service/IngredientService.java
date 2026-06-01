package com.valencia.meal.service;

import com.valencia.meal.dto.IngredientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
            HttpEntity<List<Long>> requestEntity = new HttpEntity<>(ingredientIds);

            ResponseEntity<List<IngredientDTO>> response =
                    restTemplate.exchange(
                            ingredientsUrl + "/ids",
                            HttpMethod.POST,
                            requestEntity,
                            new ParameterizedTypeReference<List<IngredientDTO>>() {}
                    );

            List<IngredientDTO> ingredients = response.getBody();

            LOG.info("Ingredients fetched successfully: {}", ingredients);
            if (ingredients == null) {
                throw new RuntimeException("Failed to fetch ingredients from external service");
            }

            return ingredients.stream().map(ingredient -> {
                IngredientDTO dto = new IngredientDTO();
                dto.setId(ingredient.getId());
                dto.setName(ingredient.getName());
                dto.setType(ingredient.getType());
                dto.setPrice(ingredient.getPrice());
                dto.setQuantity(ingredient.getQuantity());
                dto.setImage(ingredient.getImage());
                return dto;
            }).toList();

        } catch (Exception e) {
            LOG.error("Error fetching ingredients: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch ingredients from external service", e);
        }
    }


    public List<IngredientDTO> getAllIngredients() {
        LOG.info("Fetching ingredients from external service: {}", ingredientsUrl);
        ResponseEntity<List<IngredientDTO>> response =
                restTemplate.exchange(
                        ingredientsUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<IngredientDTO>>() {}
                );
        return response.getBody();
    }

    public IngredientDTO getIngredientByName(String name) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(ingredientsUrl)
                .path("/search")
                .queryParam("name", name)
                .build(true)
                .toUri();

        System.out.println("Calling Ingredient service: " + uri);

        ResponseEntity<IngredientDTO> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<IngredientDTO>() {}
        );

        return response.getBody();

    }

    public void saveAll(List<IngredientDTO> newIngredients) {
        restTemplate.postForEntity(
                ingredientsUrl + "/list",
                newIngredients,
                Void.class
        );
    }
}