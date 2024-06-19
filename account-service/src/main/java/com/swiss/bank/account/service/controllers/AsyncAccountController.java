package com.swiss.bank.account.service.controllers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.swiss.bank.account.service.services.AccountService;

@Component
public class AsyncAccountController {
	
	private AccountService accountService;
	
	public AsyncAccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@Scheduled(fixedRate = 60*1000)
	public void checkAndExecuteAccountCreation() {
		accountService.checkAndExecuteAccountCreation().subscribe();
	}

}
