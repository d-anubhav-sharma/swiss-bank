package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Preferences;

import reactor.core.publisher.Mono;

public interface PreferenceRepository extends ReactiveMongoRepository<Preferences, String> {

	Mono<Preferences> findPreferenceByUsername(String username);

	default Mono<Preferences> replace(Preferences preferences){
		return deleteByUsername(preferences.getUsername()).then(save(preferences));
	}

	Mono<Void> deleteByUsername(String username);
}
