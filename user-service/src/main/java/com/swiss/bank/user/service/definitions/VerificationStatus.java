package com.swiss.bank.user.service.definitions;

public enum VerificationStatus {

	VERIFIED("Verified"), 
	REJECTED("Rejected"), 
	NOT_PROCESSED("Not Processed Yet");

	private String label;

	VerificationStatus(String label) {
		this.label = label;
	}
	
	public String getVerificationStatus() {
		return label;
	}
	
	public boolean getVerified() {
		return "Verified".equals(label);
	}
}
