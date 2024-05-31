package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Address;

import reactor.core.publisher.Mono;

public interface AddressRepository extends ReactiveMongoRepository<Address, String> {

	Mono<Address> findAddressByUsername(String username);
	
	default Mono<Address> replace(Address address){
		return deleteByUsername(address.getUsername()).then(save(address));
	}

	Mono<Void> deleteByUsername(String username);

}
