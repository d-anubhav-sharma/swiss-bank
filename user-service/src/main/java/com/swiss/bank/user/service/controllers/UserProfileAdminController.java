package com.swiss.bank.user.service.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.swiss.bank.user.service.entities.UserProfile;
import com.swiss.bank.user.service.services.UserProfileService;

import reactor.core.publisher.Flux;

@RestController
@RestControllerAdvice
@RequestMapping("/admin/user-profile")
public class UserProfileAdminController {
	
	private UserProfileService userProfileService;
	
	public UserProfileAdminController(UserProfileService userProfileService) {
		this.userProfileService = userProfileService;
	}

	@GetMapping("/all-profiles/user")
	public ResponseEntity<Flux<UserProfile>> findAllProfilesAllowedToUser(ServerWebExchange serverWebExchange, Principal principal){
		return ResponseEntity.ok(userProfileService.findAllProfilesAllowedToUser(serverWebExchange, principal));
	}
	
}
