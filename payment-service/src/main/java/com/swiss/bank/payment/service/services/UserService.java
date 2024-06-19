package com.swiss.bank.payment.service.services;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public interface UserService {

	Flux<String> findUsersWithAccountCreationPending();

}
