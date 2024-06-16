package com.swiss.bank.payment.service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

	private String status;
	private String message;
}
