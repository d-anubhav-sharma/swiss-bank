package com.swiss.bank.payment.service.serviceimpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.swiss.bank.payment.service.definitions.PaymentCategory;
import com.swiss.bank.payment.service.definitions.PaymentStatus;
import com.swiss.bank.payment.service.entities.Payment;
import com.swiss.bank.payment.service.models.PaymentRequest;
import com.swiss.bank.payment.service.repositories.PaymentRepository;
import com.swiss.bank.payment.service.services.PaymentService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private Gson gson;
	private PaymentRepository paymentRepository;

	public PaymentServiceImpl(Gson gson, @Value("${swiss.payment.api.key}") String swissPaymentApiKey,
			PaymentRepository paymentRepository) {
		Stripe.apiKey = swissPaymentApiKey;
		this.paymentRepository = paymentRepository;
		this.gson = gson;
	}

	@Override
	public Mono<String> doPayment(PaymentRequest paymentRequest) throws StripeException {
		Payment payment = Payment
				.builder()
				.paymentRequest(paymentRequest)
				.paymentStartedAt(new Date())
				.status(PaymentStatus.REQUESTED)
				.build();
		return paymentRepository.save(payment).map(pay -> {
			try {
				payment.setId(pay.getId());
				return PaymentIntent
						.create(PaymentIntentCreateParams
									.builder()
									.setAmount(paymentRequest.getAmount())
									.setCurrency(paymentRequest.getCurrency())
									.setPaymentMethod(paymentRequest.getPaymentMethod())
						.build());
			} catch (StripeException e) {
				e.printStackTrace();
			}
			return null;
		}).flatMap(intent -> {
			payment.setPaymentIntentCreatedAt(new Date());
			payment.setStatus(PaymentStatus.INTENT_APPROVED);
			payment.setIntendId(intent.getId());
			return paymentRepository
					.save(payment)
					.map(pay -> gson.toJson(intent));
		});
	}

	@Override
	public Flux<Payment> findPaymentsWithCategoryAndStatus(PaymentCategory category, PaymentStatus status) {
		return paymentRepository.findAllPaymentsByCategoryAndStatus(category.toString(), status.toString());
	}

	@Override
	public Mono<Long> findInitialAmountPaid(String username) {
		return paymentRepository
				.findAllPaymentsByCategoryAndStatus(PaymentCategory.ACCOUNT_CREATE.toString(), PaymentStatus.PAYMENT_SUCCESS.toString())
				.map(payment -> payment.getPaymentRequest().getAmount())
				.reduce((amount1, amount2) -> amount1+amount2);
	}

	@Override
	public void checkAndUpdatePaymentStatus() {
		paymentRepository
			.findAllPaymentsByStatus(PaymentStatus.INTENT_APPROVED)
			.flatMap(payment -> {
				PaymentIntent intent;
				try {
					intent = PaymentIntent.retrieve(payment.getIntendId());
					if(intent.getStatus().equals("succeeded")) {
						payment.setStatus(PaymentStatus.PAYMENT_SUCCESS);
					}
				} catch (StripeException e) {
					log.atError().log("Error while fetching payment details: {} \n {}", e.getMessage(), e.getLocalizedMessage());
				}
				return paymentRepository.save(payment);
			}).log().subscribe();
	}

	@Override
	public Flux<Payment> findAllPayments(String username) {
		return paymentRepository.findAllByUsername(username);
	}

}
