package com.swiss.bank.payment.service.services;

import com.stripe.exception.StripeException;
import com.swiss.bank.payment.service.models.PaymentRequest;

import reactor.core.publisher.Mono;

public interface PaymentService {

	public Mono<String> doPayment(PaymentRequest paymentRequest) throws StripeException;
	
}
