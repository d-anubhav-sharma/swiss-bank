package com.swiss.bank.user.service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.models.UserUpdateRequest;
import com.swiss.bank.user.service.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/user")
public class UserController {
	
	UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/find/{username}")
	public ResponseEntity<Mono<User>> getUserByUsername(@PathVariable String username){
		return ResponseEntity.ok(userService.findUserByUsername(username));
	}
	
	@GetMapping("/all")
	public ResponseEntity<Flux<User>> findAllUsers(){
		return ResponseEntity.ok(userService.findAllUsers());
	}
	
	@PostMapping("/update")
	public ResponseEntity<Mono<User>> updateUser(@RequestBody UserUpdateRequest user){
		return ResponseEntity.ok(userService.saveUserWithRoles(user));
	}
	
}
