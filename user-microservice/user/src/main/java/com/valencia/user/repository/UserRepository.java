package com.valencia.user.repository;

import com.valencia.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    List<User> findByName(String name);

    List<User> findByEmailIs(String email);
}
