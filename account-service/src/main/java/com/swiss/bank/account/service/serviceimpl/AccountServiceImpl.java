package com.swiss.bank.account.service.serviceimpl;

import org.springframework.stereotype.Service;

import com.swiss.bank.account.service.entities.Account;
import com.swiss.bank.account.service.services.AccountService;

import reactor.core.publisher.Flux;

@Service
public class AccountServiceImpl implements AccountService{

	@Override
	public Flux<Account> findAccountsForUser() {
		return Flux.empty();
	}

}
