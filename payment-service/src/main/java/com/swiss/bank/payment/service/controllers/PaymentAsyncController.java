package com.swiss.bank.payment.service.controllers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.swiss.bank.payment.service.services.PaymentService;
import com.swiss.bank.payment.service.services.UserService;

@Component
public class PaymentAsyncController {
	
	private UserService userService;
	private PaymentService paymentService;

	public PaymentAsyncController(PaymentService paymentService, UserService userService) {
		this.paymentService = paymentService;
		this.userService = userService;
	}
	
	@Scheduled(fixedRate = 60*1000)
	public void checkAndUpdatePaymentStatus() {
		paymentService.checkAndUpdatePaymentStatus();
	}
}
