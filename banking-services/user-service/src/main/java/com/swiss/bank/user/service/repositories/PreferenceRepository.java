package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Preferences;

public interface PreferenceRepository extends ReactiveMongoRepository<Preferences, String> {

}
