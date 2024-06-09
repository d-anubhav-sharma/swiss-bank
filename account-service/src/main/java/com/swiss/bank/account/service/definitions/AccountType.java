package com.swiss.bank.account.service.definitions;

public enum AccountType {

	CURRENT("Current"),
	SAVINGS("Savings"),;

	private String accountTypeLabel;
	
	AccountType(String accountType) {
		accountTypeLabel = accountType;
	}
	
	public String getAccountType() {
		return accountTypeLabel;
	}
	
}
