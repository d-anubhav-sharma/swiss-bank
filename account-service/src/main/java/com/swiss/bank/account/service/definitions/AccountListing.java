package com.swiss.bank.account.service.definitions;

public enum AccountListing {
	
	GREEN, // VIPs
	WHITE, // Normal Accounts
	GREY, // Normal Accounts Under watch
	BLACK, // Normal Accounts blocked by government authority
	RED, // Confidential Accounts 
				// - created only for Indian Government
				// - should not be accessible to general staff
}
