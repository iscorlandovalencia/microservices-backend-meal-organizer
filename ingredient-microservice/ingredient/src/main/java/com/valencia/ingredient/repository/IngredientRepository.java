package com.valencia.ingredient.repository;

import com.valencia.ingredient.entity.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, Long> {
}
