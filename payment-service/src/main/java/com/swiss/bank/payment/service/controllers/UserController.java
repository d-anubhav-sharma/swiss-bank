package com.swiss.bank.payment.service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.payment.service.services.UserService;

import reactor.core.publisher.Flux;

@RestController
@RestControllerAdvice
@RequestMapping("/user")
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/findUsersWithAccountCreationPending")
	public Flux<String> findUsersWithAccountCreationPending(){
		return userService.findUsersWithAccountCreationPending();
	}
}
