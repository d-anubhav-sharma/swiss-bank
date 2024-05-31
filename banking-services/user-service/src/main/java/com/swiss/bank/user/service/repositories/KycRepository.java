package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Kyc;

import reactor.core.publisher.Mono;

public interface KycRepository extends ReactiveMongoRepository<Kyc, String> {

	Mono<Kyc> findKycByUsername(String username);

	default Mono<Kyc> replace(Kyc kyc){
		return deleteByUsername(kyc.getUsername()).then(save(kyc));
	}

	Mono<Void> deleteByUsername(String username);
}
