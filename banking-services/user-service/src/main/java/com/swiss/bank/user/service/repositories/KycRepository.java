package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Kyc;

public interface KycRepository extends ReactiveMongoRepository<Kyc, String> {

}
