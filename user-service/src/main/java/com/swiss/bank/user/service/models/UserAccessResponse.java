package com.swiss.bank.user.service.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAccessResponse {
	
	private String username;
	private List<RolePrivilegeResponse> roles;

}
