package com.valencia.meal.repository;

import com.valencia.meal.entity.Meal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends MongoRepository<Meal, Long>  {

}
