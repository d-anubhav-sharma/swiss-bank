package com.swiss.bank.user.service.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutAllDevicesRequest {

	private String id;
	private List<LogoutRequest> allRequests;
	
}
