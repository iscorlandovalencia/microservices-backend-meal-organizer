package com.valencia.ingredient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valencia.ingredient.dto.IngredientDTO;
import com.valencia.ingredient.entity.Ingredient;
import com.valencia.ingredient.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class IngredientServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createIngredient_shouldPersistAndReturnIngredient() throws Exception {
        // Arrange: build ingredient request
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Aceite");
        ingredient.setType("Condiment");
        ingredient.setPrice(3.5);
        ingredient.setQuantity(1.0);
        ingredient.setImage("aceite.jpg");

        // Mock repository save behavior
        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setName("Aceite");
        savedIngredient.setType("Condiment");
        savedIngredient.setPrice(3.5);
        savedIngredient.setQuantity(1.0);
        savedIngredient.setImage("aceite.jpg");

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(savedIngredient);

        // Act & Assert: perform POST
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingredient)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value("Aceite"))
                .andExpect((ResultMatcher) jsonPath("$.type").value("Condiment"))
                .andExpect((ResultMatcher) jsonPath("$.price").value(3.5))
                .andExpect((ResultMatcher) jsonPath("$.quantity").value(1.0))
                .andExpect((ResultMatcher) jsonPath("$.image").value("aceite.jpg"));
    }

    @Test
    void test_post_create_ingredient_bad_request() {
        String invalidIngredient = "badRequest : {\"name\": \"zanahoria\"}";

        try {
            mockMvc.perform(post("/api/ingredients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidIngredient))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_return_all_ingredients() {
        Ingredient ingredient = new Ingredient(1L, "zanahoria", "verdura", 8.0, 1.0, "sdhvbkdbvjbvj.jpg");
        List<Ingredient> ingredients = List.of(ingredient);

        given(ingredientRepository.findAll()).willReturn(ingredients);

        List<IngredientDTO> result = ingredientService.getAllIngredients();

        assertEquals(1, result.size());
        assertEquals("zanahoria", result.get(0).getName());
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    void when_given_id_should_return_ingredient_if_found() {
        Ingredient ingredient = new Ingredient(1L, "zanahoria", "verdura", 8.0, 1.0, "sdhvbkdbvjbvj.jpg");

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        IngredientDTO result = ingredientService.getIngredientById(1L);

        assertEquals("zanahoria", result.getName());
        verify(ingredientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetIngredientsByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        Ingredient ingredient1 = new Ingredient(1L, "Aceite", "Condiment", 3.5, 1.0, "aceite.jpg");
        Ingredient ingredient2 = new Ingredient(2L, "Aguacate", "Fruit", 2.0, 0.5, "aguacate.jpg");

        when(ingredientRepository.findAllById(ids)).thenReturn(Arrays.asList(ingredient1, ingredient2));

        List<IngredientDTO> result = ingredientService.getIngredientsByIds(ids);

        assertEquals(2, result.size());
        assertEquals("Aceite", result.get(0).getName());
        assertEquals("Aguacate", result.get(1).getName());
        verify(ingredientRepository, times(1)).findAllById(ids);
    }
}