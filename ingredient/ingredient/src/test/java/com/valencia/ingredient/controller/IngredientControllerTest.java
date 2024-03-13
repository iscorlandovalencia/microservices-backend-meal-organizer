package com.valencia.ingredient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valencia.ingredient.entity.Ingredient;
import com.valencia.ingredient.service.IngredientService;
import com.valencia.ingredient.util.JsonUtil;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(IngredientController.class)
class IngredientControllerTest {

    public static final String URL_TEMPLATE = "/api";
    public static final String INGREDIENTS = "/ingredients";
    public static final String INGREDIENT = "/ingredient";
    public static final String CEBOLLA = "cebolla";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientService ingredientService;

    Map<String, Boolean> response = new HashMap<>();
    private static final Ingredient ingredient = new Ingredient();
    private static final Ingredient ingredient2 = new Ingredient();

    @Before
    public void init() {
        response.put("deleted", Boolean.TRUE);

        ingredient.setId(89L);
        ingredient.setName(CEBOLLA);
        ingredient.setType("verdura");
        ingredient.setPrice(10.0);
        ingredient.setImage("sdhvbkdbvjbvj.jpg");

        ingredient2.setId(89L);
        ingredient2.setName("jitomate");
        ingredient2.setType("verdura");
        ingredient2.setPrice(10.0);
        ingredient2.setImage("sdhvbkdbvjbvj.jpg");
    }

    @Test
    void return_all_ingredients_given_all_ingredients() throws Exception {
        List<Ingredient> allIngredients = Arrays.asList(ingredient, ingredient2);
        given(ingredientService.getAllIngredients()).willReturn(allIngredients);
        mockMvc.perform(get(URL_TEMPLATE + INGREDIENTS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(ingredient.getName()));
    }

    @Test
    void given_ingredient_id_perform_get_ingredient_by_id() throws Exception {
        Ingredient theIngredient = new Ingredient();
        theIngredient.setId(89L);
        theIngredient.setName(CEBOLLA);

        given(ingredientService.getIngredientById(theIngredient.getId())).willReturn(Optional.ofNullable(theIngredient));
        mockMvc.perform(get(URL_TEMPLATE + INGREDIENT + "/" + theIngredient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(theIngredient.getName()));
    }

    @Test
    void given_an_ingredient_create_ingredient() throws Exception {
        Ingredient thisIngredient = new Ingredient();
        thisIngredient.setId(89L);
        thisIngredient.setName(CEBOLLA);

        given(ingredientService.createIngredient(thisIngredient)).willReturn(thisIngredient);
        mockMvc.perform(post(URL_TEMPLATE + INGREDIENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(thisIngredient)))
                .andExpect(status().isOk());
    }

    @Test
    void update_when_put_ingredient() throws Exception {
        Ingredient thisIngredient = new Ingredient();
        thisIngredient.setId(89L);
        thisIngredient.setName(CEBOLLA);

        given(ingredientService.updateIngredient(thisIngredient.getId(), thisIngredient)).willReturn(thisIngredient);
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(put(URL_TEMPLATE + INGREDIENT + "/" + thisIngredient.getId())
                        .content(mapper.writeValueAsString(thisIngredient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //.andExpect(jsonPath("name").value(thisIngredient.getName()));
    }

    @Test
    public void do_return_true_when_ingredient_id_perform_delete() throws Exception {
        doReturn(response).when(ingredientService).deleteIngredient((ingredient.getId()));
        mockMvc.perform(delete(URL_TEMPLATE + INGREDIENT + ingredient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .equals("deleted" + Boolean.TRUE);
    }

    @Test
    public void should_throw_exception_when_user_does_not_exist() throws Exception {
        Ingredient thisIngredient = new Ingredient();
        thisIngredient.setId(89L);
        thisIngredient.setName(CEBOLLA);

        Mockito.doThrow(new Exception(thisIngredient.getId().toString()))
                .when(ingredientService)
                .deleteIngredient(thisIngredient.getId());
        mockMvc.perform(delete(URL_TEMPLATE + INGREDIENT + thisIngredient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}