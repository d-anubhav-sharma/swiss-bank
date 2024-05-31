package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Occupation;

import reactor.core.publisher.Mono;

public interface OccupationRepository extends ReactiveMongoRepository<Occupation, String> {

	Mono<Occupation> findOccupationByUsername(String username);

	default Mono<Occupation> replace(Occupation occupation){
		return deleteByUsername(occupation.getUsername()).then(save(occupation));
	}

	Mono<Void> deleteByUsername(String username);
}
