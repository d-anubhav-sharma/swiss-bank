package com.swiss.bank.user.service.services;

import com.swiss.bank.user.service.entities.UserProfile;
import com.swiss.bank.user.service.models.UpdateUserProfileRequest;

import reactor.core.publisher.Mono;

public interface UserProfileService {
	
	public Mono<UserProfile> saveUserProfile(UpdateUserProfileRequest updateUserProfileRequest, String username);

	public Mono<UserProfile> getUserProfileById(String profileId);

	public Mono<UserProfile> getUserProfileByUsername(String username);
	
	
}
