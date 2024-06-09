package com.swiss.bank.account.service.services;

import com.swiss.bank.account.service.entities.Account;

import reactor.core.publisher.Flux;

public interface AccountService {

	Flux<Account> findAccountsForUser();

}
