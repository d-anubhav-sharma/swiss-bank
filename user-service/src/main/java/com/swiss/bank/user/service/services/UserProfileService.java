package com.swiss.bank.user.service.services;

import java.security.Principal;

import org.springframework.web.server.ServerWebExchange;

import com.swiss.bank.user.service.entities.UserProfile;
import com.swiss.bank.user.service.models.UpdateUserProfileRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserProfileService {
	
	public Mono<UserProfile> saveUserProfile(UpdateUserProfileRequest updateUserProfileRequest, String username);

	public Mono<UserProfile> getUserProfileById(String profileId);

	public Mono<UserProfile> getUserProfileByUsername(String username);

	public Flux<UserProfile> findAllProfilesAllowedToUser(ServerWebExchange serverWebExchange, Principal principal);
	
	
}
