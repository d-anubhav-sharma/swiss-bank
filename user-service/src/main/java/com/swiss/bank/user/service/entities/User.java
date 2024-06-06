package com.swiss.bank.user.service.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class User {

	@Id
	private String id;
	private String userId;
	@Indexed(unique = true)
	@Nonnull
	private String username;
	@JsonIgnore
	@Nonnull
	private String password;
	@Nonnull
	@Indexed(unique = true)
	private String email;
	@Indexed(unique = true)
	private String phone;
	@DocumentReference()
	private List<Role> roles;

}
