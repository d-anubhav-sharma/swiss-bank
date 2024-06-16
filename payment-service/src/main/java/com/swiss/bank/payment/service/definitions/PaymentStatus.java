package com.swiss.bank.payment.service.definitions;

/**
 * <p>The flow is as follows:</P>
 * 
 * <ol>
 * 
 * <p>Success statuses</p>
 * 	<li>REQUESTED: User hits start payment - intent is created</li>
 * 	<li>AWAITING_INTENT_APPROVAL: Gateway is supposed to confirm the payment method</li>
 * 	<li>INTENT_APPROVED: payment gateway accepts the payment initiation request and sends back the key</li>
 * 	<li>AWAITING_PAYMENT_CONFIRMATION: immediately after GATEWAY_APPROVED before user fills card details</li>
 * 	<li>PAYMENT_CONFIRMED: gateway has confirmed that they received the payment</li>
 * 	<li>PAYMENT_SUCCESS: payment has reached the target party</li>
 * 
 * <p>Error statuses</p>
 * 	<li>BAD_REQUEST: Requested payment has invalid/incorrect details</li>
 * 	<li>INTENT_REJECTED: Gateway didn't allow the payment to process</li>
 * 	<li>PAYMENT_DENIED: Gateway denied/rejected the payment</li>
 * 	<li>PAYMENT_FAILURE: Payment failed</li>
 * </ol>
 * 
 */
public enum PaymentStatus {

	 REQUESTED("Requested"),
	 AWAITING_INTENT_APPROVAL("Awaiting Intent Approval"),
	 INTENT_APPROVED("Intent Approved"),
	 AWAITING_PAYMENT_CONFIRMATION("Awaiting payment confirmation"),
	 PAYMENT_CONFIRMED("Payment confirmed"),
	 PAYMENT_SUCCESS("Payment Success"),
	 BAD_REQUEST("Bad Payment Request"),
	 INTENT_REJECTED("Payment Intent Rejected"),
	 PAYMENT_DENIED("Gateway denied payment"),
	 PAYMENT_FAILURE("Payment failed");
	
	private String status;

	PaymentStatus(String status) {
		this.status = status;
	}
	
	public String getPaymentStatus() {
		return status;
	}
}
