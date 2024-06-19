package com.swiss.bank.payment.service.serviceimpl;

import org.springframework.stereotype.Service;

import com.swiss.bank.payment.service.definitions.PaymentCategory;
import com.swiss.bank.payment.service.definitions.PaymentStatus;
import com.swiss.bank.payment.service.services.PaymentService;
import com.swiss.bank.payment.service.services.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private PaymentService paymentService;
	
	public UserServiceImpl(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@Override
	public Flux<String> findUsersWithAccountCreationPending() {
		log.atInfo().log("findUsersWithAccountCreationPending");
		return paymentService.findPaymentsWithCategoryAndStatus(PaymentCategory.ACCOUNT_CREATE, PaymentStatus.PAYMENT_SUCCESS).map(payment -> payment.getPaymentRequest().getUsername());
	}

}
