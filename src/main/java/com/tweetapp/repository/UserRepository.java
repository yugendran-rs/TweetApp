package com.tweetapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByEmailAndPassword(String email, String password);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);
}
