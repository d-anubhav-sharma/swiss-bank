package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Privilege;

import reactor.core.publisher.Mono;

public interface PrivilegeRepository extends ReactiveMongoRepository<Privilege, String>{

	Mono<Privilege> findPrivilegeByPrivilegeName(String privilegeName);

}
