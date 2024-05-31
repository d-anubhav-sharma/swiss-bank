package com.swiss.bank.user.service.services.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.Address;
import com.swiss.bank.user.service.entities.BasicInfo;
import com.swiss.bank.user.service.entities.Consent;
import com.swiss.bank.user.service.entities.Kyc;
import com.swiss.bank.user.service.entities.Occupation;
import com.swiss.bank.user.service.entities.Preferences;
import com.swiss.bank.user.service.entities.UserProfile;
import com.swiss.bank.user.service.models.UpdateUserProfileRequest;
import com.swiss.bank.user.service.repositories.AddressRepository;
import com.swiss.bank.user.service.repositories.BasicInfoRepository;
import com.swiss.bank.user.service.repositories.ConsentRepository;
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
	ConsentRepository consentRepository;
	
	public UserProfileServiceImpl(
			UserProfileRepository userProfileRepository,
			AddressRepository addressRepository,
			BasicInfoRepository basicInfoRepository,
			KycRepository kycRepository,
			OccupationRepository occupationRepository,
			PreferenceRepository preferenceRepository,
			ConsentRepository consentRepository
		) {
		this.userProfileRepository = userProfileRepository;
		this.addressRepository = addressRepository;
		this.basicInfoRepository = basicInfoRepository;
		this.kycRepository = kycRepository;
		this.occupationRepository = occupationRepository;
		this.preferenceRepository = preferenceRepository;
		this.consentRepository = consentRepository;
	}
	
	@Override
	public Mono<UserProfile> saveUserProfile(UpdateUserProfileRequest updateUserProfileRequest, String username) {
		Address address = updateUserProfileRequest.getAddress();
		BasicInfo basicInfo = updateUserProfileRequest.getBasicInfo();
		Kyc kyc = updateUserProfileRequest.getKyc();
		Occupation occupation = updateUserProfileRequest.getOccupation();
		Preferences preference = DataUtil.getOrDefault(updateUserProfileRequest.getPreferences(), new Preferences());
		Consent consent = DataUtil.getOrDefault(updateUserProfileRequest.getConsent(), new Consent());
		address.setUsername(username);
		basicInfo.setUsername(username);
		kyc.setUsername(username);
		occupation.setUsername(username);
		preference.setUsername(username);
	    Mono<Address> addressMono = addressRepository.replace(address);
	    Mono<BasicInfo> basicInfoMono = basicInfoRepository.replace(basicInfo);
	    Mono<Kyc> kycMono = kycRepository.replace(kyc);
	    Mono<Occupation> occupationMono = occupationRepository.replace(occupation);
	    Mono<Preferences> preferencesMono = preferenceRepository.replace(preference);
	    Mono<Consent> consentMono = consentRepository.replace(consent);
	    return userProfileRepository.findById(DataUtil.getOrDefault(updateUserProfileRequest.getUserId(), ""))
	            .defaultIfEmpty(UserProfile.builder().username(username).createdAt(new Date()).build())
	            .flatMap(userProfile -> Mono.zip(addressMono, basicInfoMono, kycMono, occupationMono, preferencesMono, consentMono)
	                    .flatMap(tuple -> {
	                    	userProfile.setUsername(username);
	                        userProfile.setAddress(tuple.getT1());
	                        userProfile.setBasicInfo(tuple.getT2());
	                        userProfile.setKyc(tuple.getT3());
	                        userProfile.setOccupation(tuple.getT4());
	                        userProfile.setPreferences(tuple.getT5());
	                        userProfile.setConsent(tuple.getT6());
	                        return userProfileRepository.replace(userProfile);
	                    })
	            );
	}


	@Override
	public Mono<UserProfile> getUserProfileById(String profileId) {
		return userProfileRepository.findById(profileId);
	}

	@Override
	public Mono<UserProfile> getUserProfileByUsername(String username) {
		Mono<BasicInfo> basicInfoMono = basicInfoRepository.findBasicInfoByUsername(username);
		Mono<Kyc> kycMono = kycRepository.findKycByUsername(username);
		Mono<Occupation> occupationMono = occupationRepository.findOccupationByUsername(username);
		Mono<Preferences> preferenceMono = preferenceRepository.findPreferenceByUsername(username);
		Mono<Address> addressMono = addressRepository.findAddressByUsername(username);
		Mono<Consent> consentMono = consentRepository.findConsentByUsername(username);
		
		return userProfileRepository
				.findByUsername(username)
				.flatMap(userProfile -> Mono.zip(basicInfoMono, kycMono, occupationMono, preferenceMono, addressMono, consentMono)
						.map(tuple -> {
	                    	userProfile.setUsername(username);
	                        userProfile.setAddress(tuple.getT5());
	                        userProfile.setBasicInfo(tuple.getT1());
	                        userProfile.setKyc(tuple.getT2());
	                        userProfile.setOccupation(tuple.getT3());
	                        userProfile.setPreferences(tuple.getT4());
	                        userProfile.setConsent(tuple.getT6());
	                        return userProfile;
	                    }))
				.defaultIfEmpty(blankUserProfile())
				.log();
	}

	private UserProfile blankUserProfile() {
		return UserProfile.builder()
				.username("")
				.address(new Address())
				.basicInfo(new BasicInfo())
				.kyc(new Kyc())
				.occupation(new Occupation())
				.preferences(new Preferences())
				.consent(new Consent())
				.build();
	}

}
