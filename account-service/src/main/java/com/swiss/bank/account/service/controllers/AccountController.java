package com.swiss.bank.account.service.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.account.service.entities.Account;
import com.swiss.bank.account.service.models.AccountCreationRequest;
import com.swiss.bank.account.service.services.AccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/account")
public class AccountController {
	
	private AccountService accountService;
	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping("/checkUserHasExistingAccount")
	public ResponseEntity<Flux<String>> checkUserHasExistingAccount(@RequestParam("includeInactiveAccounts") boolean includeInactiveAccounts, Principal principal){
		return ResponseEntity.ok(accountService.checkUserHasExistingAccount(includeInactiveAccounts, principal.getName()));
	}

	@PostMapping("/create")
	public ResponseEntity<Mono<Account>> createAccount(@RequestBody AccountCreationRequest accountCreationRequest){
		return ResponseEntity.ok(accountService.createAccount(accountCreationRequest));
	}
}
