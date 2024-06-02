package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.models.LoginRequest;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

	Mono<User> findUserByUsername(String username);

	@Query("{username: ?username, password: ?password}")
	Mono<User> findUserByUsernameAndPassword(LoginRequest loginRequest);

}
