package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.UserProfile;

import reactor.core.publisher.Mono;

public interface UserProfileRepository extends ReactiveMongoRepository<UserProfile, String> {

	Mono<UserProfile> findByUserId(String userId);

	Mono<UserProfile> findByUsername(String username);

}
