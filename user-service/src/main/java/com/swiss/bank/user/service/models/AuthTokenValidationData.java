package com.swiss.bank.user.service.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokenValidationData {

	private String username;
	private String userId;
	private String uuid;
	private List<String> roles;
	
}
