package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Occupation;

public interface OccupationRepository extends ReactiveMongoRepository<Occupation, String> {

}
