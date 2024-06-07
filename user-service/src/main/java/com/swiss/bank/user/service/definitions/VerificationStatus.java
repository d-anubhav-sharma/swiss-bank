package com.swiss.bank.user.service.definitions;

public enum VerificationStatus {

	VERIFIED("Verified"), 
	REJECTED("Rejected"), 
	NOT_PROCESSED("Not Processed Yet");

	String label;

	VerificationStatus(String label) {
		this.label = label;
	}
}
