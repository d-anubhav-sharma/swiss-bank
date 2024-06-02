package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Consent;

import reactor.core.publisher.Mono;

public interface ConsentRepository extends ReactiveMongoRepository<Consent, String>{

	Mono<Consent> findConsentByUsername(String username);

	default Mono<Consent> replace(Consent consent){
		return deleteByUsername(consent.getUsername()).then(save(consent));
	}

	Mono<Void> deleteByUsername(String username);
}
