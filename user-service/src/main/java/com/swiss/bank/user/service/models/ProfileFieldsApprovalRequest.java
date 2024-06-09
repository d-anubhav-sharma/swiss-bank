package com.swiss.bank.user.service.models;

import com.swiss.bank.user.service.definitions.VerificationStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileFieldsApprovalRequest {
	
	private String fieldName;
	private String username;
	private VerificationStatus status;

}
