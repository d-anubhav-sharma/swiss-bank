package com.swiss.bank.payment.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.payment.service.entities.Payment;

public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {

}
