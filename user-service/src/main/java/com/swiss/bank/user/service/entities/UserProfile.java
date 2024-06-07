package com.swiss.bank.user.service.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.swiss.bank.user.service.definitions.VerificationStatus;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class UserProfile {

	@Id
	private String id;
	private String userId;
	@Nonnull
	private String username;
	@DocumentReference
	private BasicInfo basicInfo;
	@DocumentReference
	private Address address;
	@DocumentReference
	private Occupation occupation;
	@DocumentReference
	private Kyc kyc;
	private Date createdAt;
	private Date lastLoginAt;
	@DocumentReference
	private Preferences preferences;
	@DocumentReference
	private Consent consent;
	@Builder.Default
	private VerificationStatus emailVerified = VerificationStatus.NOT_PROCESSED;
	@Builder.Default
	private VerificationStatus governmentIdVerified = VerificationStatus.NOT_PROCESSED;
	@Builder.Default
	private VerificationStatus dateofBirthVerified = VerificationStatus.NOT_PROCESSED;
	@Builder.Default
	private VerificationStatus addressVerified = VerificationStatus.NOT_PROCESSED;
	@Builder.Default
	private VerificationStatus occupationVerified = VerificationStatus.NOT_PROCESSED;
	@Builder.Default
	private VerificationStatus phoneVerified = VerificationStatus.NOT_PROCESSED;
	@Builder.Default
	private VerificationStatus photoVerified = VerificationStatus.NOT_PROCESSED;
	
}
