package com.swiss.bank.account.service.services;

import com.swiss.bank.account.service.entities.Account;
import com.swiss.bank.account.service.models.AccountCreationRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

	Flux<Account> findAccountsForUser(String username);
	Flux<Account> checkAndExecuteAccountCreation();
	Flux<String> checkUserHasExistingAccount(boolean includeInactiveAccounts, String username);
	Mono<Account> createAccount(AccountCreationRequest accountCreationRequest);

}
