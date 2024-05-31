package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.BasicInfo;

public interface BasicInfoRepository extends ReactiveMongoRepository<BasicInfo, String> {

}
