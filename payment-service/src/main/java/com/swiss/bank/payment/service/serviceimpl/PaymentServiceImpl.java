package com.swiss.bank.payment.service.serviceimpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.swiss.bank.payment.service.definitions.PaymentStatus;
import com.swiss.bank.payment.service.entities.Payment;
import com.swiss.bank.payment.service.models.PaymentRequest;
import com.swiss.bank.payment.service.repositories.PaymentRepository;
import com.swiss.bank.payment.service.services.PaymentService;

import reactor.core.publisher.Mono;

@Service
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
			return paymentRepository
					.save(payment)
					.map(pay -> gson.toJson(intent));
		});
	}

}
