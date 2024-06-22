package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.Role;

import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveMongoRepository<Role, String>{

	Mono<Role> findRoleByRoleName(String roleName);

}
