package com.swiss.bank.payment.service.services;

import com.stripe.exception.StripeException;
import com.swiss.bank.payment.service.definitions.PaymentCategory;
import com.swiss.bank.payment.service.definitions.PaymentStatus;
import com.swiss.bank.payment.service.entities.Payment;
import com.swiss.bank.payment.service.models.PaymentRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentService {

	public Mono<String> doPayment(PaymentRequest paymentRequest) throws StripeException;

	public Flux<Payment> findPaymentsWithCategoryAndStatus(PaymentCategory accountCreate, PaymentStatus paymentSuccess);

	public Mono<Long> findInitialAmountPaid(String username);

	public void checkAndUpdatePaymentStatus();

	public Flux<Payment> findAllPayments(String name);
	
}
