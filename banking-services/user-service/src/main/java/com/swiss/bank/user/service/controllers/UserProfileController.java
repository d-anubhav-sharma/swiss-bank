package com.swiss.bank.user.service.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.entities.UserProfile;
import com.swiss.bank.user.service.models.UpdateUserProfileRequest;
import com.swiss.bank.user.service.services.UserProfileService;

import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/user-profile")
public class UserProfileController {
	
	UserProfileService userProfileService;
	
	public UserProfileController(UserProfileService userProfileService) {
		this.userProfileService = userProfileService;
	}
	
	@PostMapping("/save")
	public ResponseEntity<Mono<UserProfile>> saveUserProfile(@RequestBody UpdateUserProfileRequest updateUserProfileRequest, Principal principal){
		String username = principal.getName();
		if(!updateUserProfileRequest.getBasicInfo().getUsername().isBlank()) {
			username = updateUserProfileRequest.getBasicInfo().getUsername();
		}
		return ResponseEntity.ok(userProfileService.saveUserProfile(updateUserProfileRequest, username));
	}

	@GetMapping("/profileId/{profileId}")
	public ResponseEntity<Mono<UserProfile>> getUserProfileById(@PathVariable String profileId){
		return ResponseEntity.ok(userProfileService.getUserProfileById(profileId));
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<Mono<UserProfile>> getUserProfileByUsername(@PathVariable String username){
		return ResponseEntity.ok(userProfileService.getUserProfileByUsername(username));
	}
}
