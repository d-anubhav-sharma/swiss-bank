package com.swiss.bank.payment.service.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.swiss.bank.payment.service.definitions.PaymentStatus;
import com.swiss.bank.payment.service.models.PaymentRequest;
import com.swiss.bank.payment.service.models.PaymentResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Payment {

	@Id
	private String id;
	private PaymentRequest paymentRequest;
	private PaymentResponse paymentResponse;
	private Date paymentStartedAt;
	private Date paymentIntentCreatedAt;
	private Date paymentCompletedAt;
	private Date paymentFailedAt;
	private PaymentStatus status;
	private String intendId;

}
