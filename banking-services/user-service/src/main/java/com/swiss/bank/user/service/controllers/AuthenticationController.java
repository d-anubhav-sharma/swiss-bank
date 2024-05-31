package com.swiss.bank.user.service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.swiss.bank.user.service.exceptions.DuplicateUsernameException;
import com.swiss.bank.user.service.models.LoginRequest;
import com.swiss.bank.user.service.models.LoginResponse;
import com.swiss.bank.user.service.models.LogoutResponse;
import com.swiss.bank.user.service.models.RegisterUserRequest;
import com.swiss.bank.user.service.models.RegisterUserResponse;
import com.swiss.bank.user.service.services.AuthenticationService;
import com.swiss.bank.user.service.services.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/auth")
public class AuthenticationController {

	AuthenticationService authenticationService;

	UserService userService;
	
	public AuthenticationController(AuthenticationService authenticationService,UserService userService) {
		this.authenticationService = authenticationService;
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<Mono<LoginResponse>> handleUserLogin(@Valid @RequestBody LoginRequest loginRequest, ServerWebExchange exchange) {
		return ResponseEntity.ok(authenticationService.login(loginRequest, exchange));
	}

	@GetMapping("/login")
	public String loginPage() {
		return "Please login using POST method with proper credentials";
	}

	@PostMapping("/register")
	public ResponseEntity<Mono<RegisterUserResponse>> register(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
		return ResponseEntity.ok(authenticationService.register(registerUserRequest));
	}
	
	@GetMapping("/checkUsernameAvailable")
	public ResponseEntity<Mono<String>> checkUsernameAvailable(@RequestParam String username){
		return ResponseEntity.ok(userService
			.findUserByUsername(username)
			.flatMap(user -> Mono.error(new DuplicateUsernameException(username)))
			.switchIfEmpty(Mono.just("success"))
			.cast(String.class));
	}
	
	@GetMapping("/logout")
	public ResponseEntity<Mono<LogoutResponse>> logout(ServerWebExchange exchange){
		return ResponseEntity.ok(authenticationService.logout(exchange));
	}

}
