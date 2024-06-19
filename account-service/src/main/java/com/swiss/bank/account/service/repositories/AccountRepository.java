package com.swiss.bank.account.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.account.service.entities.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String>{

	Flux<Account> findAllByUsername(String username);

	Mono<Account> findAccountByUsernameAndAccountType(String username, String accountType);

}
