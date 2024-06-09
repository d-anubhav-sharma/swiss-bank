package com.swiss.bank.user.service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserFromTokenRequest {

	private String username;
	private String authToken;
}
