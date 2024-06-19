package com.swiss.bank.account.service.serviceimpl;

import java.net.URI;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiss.bank.account.service.definitions.AccountListing;
import com.swiss.bank.account.service.definitions.AccountType;
import com.swiss.bank.account.service.entities.Account;
import com.swiss.bank.account.service.models.AccountCreationRequest;
import com.swiss.bank.account.service.repositories.AccountRepository;
import com.swiss.bank.account.service.services.AccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService{

	private AccountRepository accountRepository;
	private WebClient webClient;
	private String paymentServiceBaseUrl;
	private WebClient authenticatedWebClient;
	private String userServiceBaseUrl;
	
	private static final Predicate<String> STATUS_GREEN_PREDICATE = "GREEN"::equals;
	
	public AccountServiceImpl(
			AccountRepository accountRepository, 
			WebClient webClient, 
			@Value("${payment-service.base-url}") String paymentServiceBaseUrl,
			@Value("${user-service.base-url}") String userServiceBaseUrl,
			@Qualifier("authenticatedWebclient") WebClient authenticatedWebClient) {
		this.accountRepository = accountRepository;
		this.webClient = webClient;
		this.paymentServiceBaseUrl = paymentServiceBaseUrl;
		this.authenticatedWebClient = authenticatedWebClient;
		this.userServiceBaseUrl = userServiceBaseUrl;
	}
	
	@Override
	public Flux<Account> findAccountsForUser(String username) {
		return accountRepository.findAllByUsername(username);
	}

	@Override
	public Flux<Account> checkAndExecuteAccountCreation() {
		return authenticatedWebClient
				.get()
				.uri(URI.create(paymentServiceBaseUrl+"/user/findUsersWithAccountCreationPending"))
				.retrieve()
				.bodyToFlux(String.class)
				.flatMap(username -> createAccount(AccountCreationRequest
						.builder()
						.username(username)
						.accountType(AccountType.SAVINGS)
						.balance(5000)
						.build())).log();
			
	}

	@Override
	public Flux<String> checkUserHasExistingAccount(boolean includeInactiveAccounts, String username) {
		return accountRepository
				.findAllByUsername(username)
				.filter(account -> !AccountListing.RED.equals(account.getListing()))
				.filter(account -> 
					includeInactiveAccounts || 
					!(account.isDisabled()||account.isFrozen()||account.isLocked()))
				.map(Account::getAccountNumber);
				
	}

	@Override
	public Mono<Account> createAccount(AccountCreationRequest accountCreationRequest) {
		return authenticatedWebClient
			.get()
			.uri(URI.create(userServiceBaseUrl+"/user-profile/checkKYC/"+accountCreationRequest.getUsername()))
			.retrieve()
			.bodyToMono(String.class)
			.filter(STATUS_GREEN_PREDICATE)
			.switchIfEmpty(Mono.error(new Exception("KYC Check failed")))
			.flatMap(userProfileState -> checkPaymentStatus(accountCreationRequest.getUsername()))
			.filter(payment -> payment>=5000)
			.switchIfEmpty(Mono.error(new Exception("You need to pay the intial amount")))
			.flatMap(__ -> accountRepository.findAccountByUsernameAndAccountType(
						accountCreationRequest.getUsername(),
						accountCreationRequest.getAccountType().toString()
					))
			.switchIfEmpty(accountRepository.save(
					Account.builder()
						.username(accountCreationRequest.getUsername())
						.accountNumber(generateAccountNumber(accountCreationRequest.getUsername()))
						.accountType(accountCreationRequest.getAccountType())
						.balance(accountCreationRequest.getBalance())
						.listing(AccountListing.WHITE)
						.build()
					));
	}
	
	private String generateAccountNumber(String username) {
		return "5600"+System.currentTimeMillis()+sumArray(username.getBytes());
	}

	private int sumArray(byte[] bytes) {
		int sum = 0;
		for(byte b : bytes) sum+=b;
		return sum;
	}

	private Mono<Long> checkPaymentStatus(String username){
		return authenticatedWebClient
				.get()
				.uri(URI.create(paymentServiceBaseUrl+"/payment/findInitialAmountPaid/"+username))
				.retrieve()
				.bodyToMono(Long.class).log();
	}

}
