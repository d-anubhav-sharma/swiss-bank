package com.swiss.bank.user.service.models;

import java.util.List;

import com.swiss.bank.user.service.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

	private String id;
	private String username;
	private String email;
	private String phone;
	private List<Role> roles;
	
}
