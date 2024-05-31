package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Address;

public interface AddressRepository extends ReactiveMongoRepository<Address, String> {

}
