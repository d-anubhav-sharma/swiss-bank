package com.swiss.bank.user.service.services.impl;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.UserProfile;
import com.swiss.bank.user.service.models.ProfileFieldsApprovalRequest;
import com.swiss.bank.user.service.services.ApprovalService;
import com.swiss.bank.user.service.services.UserProfileService;

import reactor.core.publisher.Mono;

@Service
public class ApprovalServiceImpl implements ApprovalService {

	private UserProfileService userProfileService;
	
	public ApprovalServiceImpl(UserProfileService userProfileService) {
		this.userProfileService = userProfileService;
	}
	
	private UserProfile updateApprovalStatus(UserProfile userProfile, ProfileFieldsApprovalRequest profileFieldsApprovalRequest) {
		switch(profileFieldsApprovalRequest.getFieldName()) {
		case "email": 
			userProfile.setEmailVerified(profileFieldsApprovalRequest.getStatus());
			return userProfile;
		case "phone": 
			userProfile.setPhoneVerified(profileFieldsApprovalRequest.getStatus());
			return userProfile;
		case "dateOfBirth": 
			userProfile.setDateofBirthVerified(profileFieldsApprovalRequest.getStatus());
			return userProfile;
		case "address": 
			userProfile.setAddressVerified(profileFieldsApprovalRequest.getStatus());
			return userProfile;
		case "occupation": 
			userProfile.setOccupationVerified(profileFieldsApprovalRequest.getStatus());
			return userProfile;
		case "photo": 
			userProfile.setPhotoVerified(profileFieldsApprovalRequest.getStatus());
			return userProfile;
		case "identity":
			userProfile.setGovernmentIdVerified(profileFieldsApprovalRequest.getStatus());
			return userProfile;
		default: 
			return userProfile;
		}
	}
	
	@Override
	public Mono<String> approveProfileFields(ProfileFieldsApprovalRequest profileFieldsApprovalRequest) {
		return userProfileService
				.getUserProfileByUsername(profileFieldsApprovalRequest.getUsername())
				.map(userProfile -> updateApprovalStatus(userProfile, profileFieldsApprovalRequest))
				.flatMap(userProfile -> userProfileService.saveUserProfile(userProfile))
				.map(UserProfile::getUsername);
	}

}
