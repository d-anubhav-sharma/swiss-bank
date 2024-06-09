package com.swiss.bank.user.service.services;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.models.ProfileFieldsApprovalRequest;

import reactor.core.publisher.Mono;

@Service
public interface ApprovalService {

	Mono<String> approveProfileFields(ProfileFieldsApprovalRequest profileFieldsApprovalRequest);
	
}
