package com.swiss.bank.user.service.services.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.Address;
import com.swiss.bank.user.service.entities.BasicInfo;
import com.swiss.bank.user.service.entities.Kyc;
import com.swiss.bank.user.service.entities.Occupation;
import com.swiss.bank.user.service.entities.Preferences;
import com.swiss.bank.user.service.entities.UserProfile;
import com.swiss.bank.user.service.models.UpdateUserProfileRequest;
import com.swiss.bank.user.service.repositories.AddressRepository;
import com.swiss.bank.user.service.repositories.BasicInfoRepository;
import com.swiss.bank.user.service.repositories.KycRepository;
import com.swiss.bank.user.service.repositories.OccupationRepository;
import com.swiss.bank.user.service.repositories.PreferenceRepository;
import com.swiss.bank.user.service.repositories.UserProfileRepository;
import com.swiss.bank.user.service.services.UserProfileService;
import com.swiss.bank.user.service.util.DataUtil;

import reactor.core.publisher.Mono;

@Service
public class UserProfileServiceImpl implements UserProfileService{

	UserProfileRepository userProfileRepository;
	BasicInfoRepository basicInfoRepository;
	KycRepository kycRepository;
	OccupationRepository occupationRepository;
	PreferenceRepository preferenceRepository;
	AddressRepository addressRepository;
	
	public UserProfileServiceImpl(
			UserProfileRepository userProfileRepository,
			AddressRepository addressRepository,
			BasicInfoRepository basicInfoRepository,
			KycRepository kycRepository,
			OccupationRepository occupationRepository,
			PreferenceRepository preferenceRepository
		) {
		this.userProfileRepository = userProfileRepository;
		this.addressRepository = addressRepository;
		this.basicInfoRepository = basicInfoRepository;
		this.kycRepository = kycRepository;
		this.occupationRepository = occupationRepository;
		this.preferenceRepository = preferenceRepository;
	}
	
	@Override
	public Mono<UserProfile> saveUserProfile(UpdateUserProfileRequest updateUserProfileRequest, String username) {
		Address address = updateUserProfileRequest.getAddress();
		BasicInfo basicInfo = updateUserProfileRequest.getBasicInfo();
		Kyc kyc = updateUserProfileRequest.getKyc();
		Occupation occupation = updateUserProfileRequest.getOccupation();
		Preferences preference = DataUtil.getOrDefault(updateUserProfileRequest.getPreferences(), new Preferences());
		address.setUsername(username);
		basicInfo.setUsername(username);
		kyc.setUsername(username);
		occupation.setUsername(username);
		preference.setUsername(username);
	    Mono<Address> addressMono = addressRepository.save(address);
	    Mono<BasicInfo> basicInfoMono = basicInfoRepository.save(basicInfo);
	    Mono<Kyc> kycMono = kycRepository.save(kyc);
	    Mono<Occupation> occupationMono = occupationRepository.save(occupation);
	    Mono<Preferences> preferencesMono = preferenceRepository.save(preference);
	    return userProfileRepository.findById(DataUtil.getOrDefault(updateUserProfileRequest.getUserId(), ""))
	            .defaultIfEmpty(UserProfile.builder().username(username).createdAt(new Date()).build())
	            .flatMap(userProfile -> Mono.zip(addressMono, basicInfoMono, kycMono, occupationMono, preferencesMono)
	                    .flatMap(tuple -> {
	                    	userProfile.setUsername(username);
	                        userProfile.setAddress(tuple.getT1());
	                        userProfile.setBasicInfo(tuple.getT2());
	                        userProfile.setKyc(tuple.getT3());
	                        userProfile.setOccupation(tuple.getT4());
	                        userProfile.setPreferences(tuple.getT5());
	                        return userProfileRepository.save(userProfile);
	                    })
	            );
	}


	@Override
	public Mono<UserProfile> getUserProfileById(String profileId) {
		return userProfileRepository.findById(profileId);
	}

	@Override
	public Mono<UserProfile> getUserProfileByUsername(String username) {
		return userProfileRepository
				.findByUsername(username);
	}

}
