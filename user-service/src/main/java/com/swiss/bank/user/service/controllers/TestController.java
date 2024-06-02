package com.swiss.bank.user.service.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.models.UserUpdateRequest;
import com.swiss.bank.user.service.services.UserService;

import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/test")
public class TestController {

	private UserService userService;
	
	public TestController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/get")
	public String get() {
		return "get";
	}
	
	@PostMapping("/post")
	public String post() {
		return "post";
	}
	
	@PutMapping("/put")
	public String put() {
		return "put";
	}
	
	@DeleteMapping("/delete")
	public String delete() {
		return "delete";
	}
	
	@PostMapping("/giveAdminAccessToOwner")
	public Mono<User> giveAdminAccessToOwner(){
		Role role = new Role();
		role.setUsername("anubhav_sharma");
		role.setRoleName("OWNER");
		role.setPrivileges(List.of("STAFF", "ADMIN"));
		
		return userService.saveUserWithRoles(UserUpdateRequest
				.builder()
				.username("anubhav_sharma")
				.roles(List.of(role))
				.build());
	}
}
