package com.swiss.bank.payment.service.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.stripe.exception.StripeException;
import com.swiss.bank.payment.service.entities.Payment;
import com.swiss.bank.payment.service.models.PaymentRequest;
import com.swiss.bank.payment.service.services.PaymentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/payment")
public class PaymentController {
	
	PaymentService paymentService;
	
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping("/createPaymentIntent")
	public ResponseEntity<Mono<String>> doPayment(@RequestBody PaymentRequest paymentRequest) throws StripeException{
		return ResponseEntity.ok(paymentService.doPayment(paymentRequest));
	}
	
	@GetMapping("/findInitialAmountPaid/{username}")
	public ResponseEntity<Mono<Long>> findInitialAmountPaid(@PathVariable String username) throws StripeException{
		return ResponseEntity.ok(paymentService.findInitialAmountPaid(username));
	}
	
	@GetMapping("/all")
	public ResponseEntity<Flux<Payment>> findAllPayments(Principal principal) throws StripeException{
		return ResponseEntity.ok(paymentService.findAllPayments(principal.getName()));
	}
}
