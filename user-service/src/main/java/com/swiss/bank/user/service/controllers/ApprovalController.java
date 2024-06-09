package com.swiss.bank.user.service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.models.ProfileFieldsApprovalRequest;
import com.swiss.bank.user.service.services.ApprovalService;

import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/approval")
public class ApprovalController {
	
	private ApprovalService approvalService;
	
	ApprovalController(ApprovalService approvalService){
		this.approvalService = approvalService;
	}

	@PostMapping("/profileFields")
	public ResponseEntity<Mono<String>> approveProfileFields(@RequestBody ProfileFieldsApprovalRequest profileFieldsApprovalRequest){
		return ResponseEntity.ok(approvalService.approveProfileFields(profileFieldsApprovalRequest));
	}
}
