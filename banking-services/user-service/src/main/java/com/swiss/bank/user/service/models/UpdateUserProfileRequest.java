package com.swiss.bank.user.service.models;

import com.swiss.bank.user.service.entities.Address;
import com.swiss.bank.user.service.entities.BasicInfo;
import com.swiss.bank.user.service.entities.Consent;
import com.swiss.bank.user.service.entities.Kyc;
import com.swiss.bank.user.service.entities.Occupation;
import com.swiss.bank.user.service.entities.Preferences;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserProfileRequest {
	
	private String userId;
	private BasicInfo basicInfo;
	private Address address;
	private Occupation occupation;
	private Kyc kyc;
	private String profilePictureUrl;
	private Preferences preferences;
	private Consent consent;
	
}
