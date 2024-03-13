package com.valencia.ingredient.service;

import com.valencia.ingredient.entity.Ingredient;
import com.valencia.ingredient.repository.IngredientRepository;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class IngredientServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private static IngredientService ingredientService;

    public static final String URL_TEMPLATE = "/api";

    private static final Ingredient ingredient = new Ingredient();
    private static final Ingredient ingredient2 = new Ingredient();
    private static Ingredient ingredient3;
    private static Ingredient newIngredient;
    private static Ingredient newIngredient2;
    private static Ingredient newIngredient3;
    private static final Ingredient updateIngredient = new Ingredient();
    private static Ingredient updateIngredient2;
    private static final Ingredient deleteIngredient = new Ingredient();
    private static Ingredient deleteIngredient2;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    public IngredientServiceTest() {
        ingredient.setId(1L);
        ingredient.setName("zanahoria");
        ingredient.setType("verdura");
        ingredient.setImage("sdhvbkdbvjbvj.jpg");
        ingredient.setPrice(8.0);

        ingredient2.setId(2L);
        ingredient2.setName("zanahoria");
        ingredient2.setType("verdura");
        ingredient2.setImage("sdhvbkdbvjbvj.jpg");
        ingredient2.setPrice(8.0);

        deleteIngredient.setId(2L);
        deleteIngredient.setName("cebolla");
        deleteIngredient.setType("verdura");
        deleteIngredient.setImage("sdhvbkdbvjbvj_2.jpg");
        deleteIngredient.setPrice(12.0);

        updateIngredient.setName("zanahoria");
        updateIngredient.setType("verdura");
        updateIngredient.setImage("sdhvbkdbvjbvj.jpg");
        updateIngredient.setPrice(11.0);

    }

    @Test
    public void create_ingredient_when_perform_ingredients_path() throws Exception {
        String newIngredient = "{\n" +
                "    \"name\": \"zanahoria\",\n" +
                "    \"type\": \"verdura\",\n" +
                "    \"price\": \"8\",\n" +
                "    \"image\": \"sdhvbkdbvjbvj.jpg\"\n" +
                "}";
        mockMvc.perform(post(URL_TEMPLATE + "/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newIngredient))
                .andExpect(status().isOk());
    }

    @Test
    public void test_post_create_ingredient_bad_request() throws Exception {
        String newIngredient = "badRequest : {\n" +
                "    \"name\": \"zanahoria\",\n" +
                "    \"type\": \"verdura\",\n" +
                "    \"price\": \"8\",\n" +
                "    \"image\": \"sdhvbkdbvjbvj.jpg\"\n" +
                "}";
        mockMvc.perform(post(URL_TEMPLATE + "/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newIngredient))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_all_ingredients() {
        List<Ingredient> ingredients = new ArrayList();
        ingredients.add(new Ingredient());
        given(ingredientRepository.findAll()).willReturn(ingredients);
        List<Ingredient> expected = ingredientService.getAllIngredients();
        assertEquals(expected, ingredients);
        verify(ingredientRepository).findAll();
    }

    @Test
    public void when_given_id_should_return_ingredient_if_found() throws Exception {
        when(ingredientRepository.findById(ingredient.getId())).thenReturn(Optional.of(ingredient));
        Optional<Ingredient> expected = ingredientService.getIngredientById(ingredient.getId());
        assertEquals(ingredient, expected.get());
        verify(ingredientRepository).findById(ingredient.getId());
    }

    @Test
    public void when_given_id_should_delete_ingredient_if_found() throws Exception {
        when(ingredientRepository.findById(deleteIngredient.getId())).thenReturn(Optional.of(deleteIngredient));
        ingredientService.deleteIngredient(deleteIngredient.getId());
        verify(ingredientRepository).deleteById(deleteIngredient.getId());
    }

    @Test
    public void when_given_id_should_update_ingredient_if_found() throws Exception {
        given(ingredientRepository.findById(ingredient2.getId())).willReturn(Optional.of(ingredient2));
        ingredientService.updateIngredient(ingredient2.getId(), updateIngredient);
        verify(ingredientRepository).save(updateIngredient);
        verify(ingredientRepository).findById(updateIngredient.getId());
    }

		/*
	@Test
	public void should_throw_exception_when_ingredient_does_not_exist_on_update() throws Exception {
//		expectedEx.expect(RuntimeException.class);
//		expectedEx.expectMessage("Ingredient not found for this id :: " + updateIngredient.getId());


		Exception exception = assertThrows(Exception.class, () -> {
			throw new Exception("Ingredient not found for this id :: " + deleteIngredient.getId());
		});
		assertEquals("Ingredient not found for this id :: " + deleteIngredient.getId(), exception.getMessage());
		given(ingredientRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
		ingredientService.updateIngredient(updateIngredient.getId(), ingredient);
	}

	@Test
	public void should_throw_exception_when_user_doesnt_exist() throws Exception {
		expectedEx.expect(RuntimeException.class);
		expectedEx.expectMessage("Ingredient not found for this id :: " + newIngredient2.getId());

		given(ingredientRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
		ingredientService.getIngredientById(newIngredient2.getId());
	}

	@Test
	public void should_throw_exception_when_ingredient_does_not_exist_on_delete() throws Exception {
		Exception exception = assertThrows(Exception.class, () -> {
			throw new Exception("Ingredient not found for this id :: " + deleteIngredient2.getId());
		});
		assertEquals("Ingredient not found for this id :: " + deleteIngredient2.getId(), exception.getMessage());
		given(ingredientRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
		ingredientService.deleteIngredient(deleteIngredient2.getId());
	}*/
}