package com.swiss.bank.user.service.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogoutRequest {

	private String id;
	private String deviceId;
	private String identifier;
	
}
