package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.BasicInfo;

import reactor.core.publisher.Mono;

public interface BasicInfoRepository extends ReactiveMongoRepository<BasicInfo, String> {

	Mono<BasicInfo> findBasicInfoByUsername(String username);

	default Mono<BasicInfo> replace(BasicInfo basicInfo){
		return deleteByUsername(basicInfo.getUsername()).then(save(basicInfo));
	}

	Mono<Void> deleteByUsername(String username);
}
