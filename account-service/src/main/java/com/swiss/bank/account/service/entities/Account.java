package com.swiss.bank.account.service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.swiss.bank.account.service.definitions.AccountListing;
import com.swiss.bank.account.service.definitions.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Account {

	@Id
	private String id;
	private String accountNumber;
	private String username;
	private AccountType accountType;
	private long balance;
	private boolean locked;
	private boolean frozen;
	private boolean disabled;
	private AccountListing listing;
	
}
