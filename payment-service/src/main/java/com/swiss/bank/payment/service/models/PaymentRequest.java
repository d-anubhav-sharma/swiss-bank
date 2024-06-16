package com.swiss.bank.payment.service.models;

import com.swiss.bank.payment.service.definitions.PaymentCategory;
import com.swiss.bank.payment.service.definitions.PaymentSubCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {
	private String username;
	private String fromParty;
	private String toParty;
	private String currency;
	private long amount;
	private String paymentMethod;
	private PaymentCategory category;
	private PaymentSubCategory subCategory;
}
