package com.swiss.bank.account.service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.account.service.entities.Account;
import com.swiss.bank.account.service.services.AccountService;

import reactor.core.publisher.Flux;

@RestController
@RestControllerAdvice
@RequestMapping("/personalBanking/Account/")
public class PersonalBankingAccountController {

	private AccountService accountService;
	
	public PersonalBankingAccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<Flux<Account>> findAccountsForUser(){
		return ResponseEntity.ok(accountService.findAccountsForUser());
	}
}
