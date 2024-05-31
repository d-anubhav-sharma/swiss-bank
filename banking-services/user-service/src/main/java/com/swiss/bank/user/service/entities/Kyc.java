package com.swiss.bank.user.service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.swiss.bank.user.service.definitions.IdentificationType;

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
public class Kyc {

	@Id
	private String id;
	@Nonnull
	@Indexed(unique = true)
	private String username;
	private IdentificationType addressProofType;
	private IdentificationType identityProofType;
	private String addressProofId;
	private String identityProofId;
	private String addressProof;
	private String identityProof;
	private String personalPhoto;
	private String emailForVerification;
	private String phoneForVerification;

}
