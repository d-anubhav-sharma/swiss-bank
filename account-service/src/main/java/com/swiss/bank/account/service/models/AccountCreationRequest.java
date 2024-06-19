package com.swiss.bank.account.service.models;

import com.swiss.bank.account.service.definitions.AccountType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreationRequest {

	private String username;
	private long balance;
	private AccountType accountType;
}
