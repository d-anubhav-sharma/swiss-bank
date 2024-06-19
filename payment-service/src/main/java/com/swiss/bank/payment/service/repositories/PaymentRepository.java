package com.swiss.bank.payment.service.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.payment.service.definitions.PaymentStatus;
import com.swiss.bank.payment.service.entities.Payment;

import reactor.core.publisher.Flux;

public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {

	@Query("""
			   {"paymentRequest.category": ?0, "status": ?1}
			""")
	Flux<Payment> findAllPaymentsByCategoryAndStatus(String category, String status);

	Flux<Payment> findAllPaymentsByStatus(PaymentStatus intentApproved);

	@Query("""
			   {"paymentRequest.username": ?0}
			""")
	Flux<Payment> findAllByUsername(String username);

}
