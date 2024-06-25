package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.PathPrivilegeMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PathPrivilegeMapperRepository extends ReactiveMongoRepository<PathPrivilegeMapper, String>{

	Flux<PathPrivilegeMapper> findAllByCategory(String category);

	Mono<PathPrivilegeMapper> findByPrivilegeName(String privilegeName);

}
