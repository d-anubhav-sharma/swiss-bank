package com.swiss.bank.user.service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserResponse {
	private String username;
	private String id;
}
