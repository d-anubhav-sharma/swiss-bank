package com.swiss.bank.user.service.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequest {

	@NotBlank
	@Size(min = 3, max = 30, message = "username should be 3-30 characters long")
	@Pattern(regexp = "^\\w*$", message = "username should only contain alphanumeric characters or underscore")
	private String username;
	@NotBlank
	@Size(min=8, max=30, message = "password should be 3-30 characters long")
	@Pattern(regexp = ".*[a-z].*", message = "password should contain atleast one lowercase alphabet")
	@Pattern(regexp = ".*[A-Z].*", message = "password should contain atleast one uppercase alphabet")
	@Pattern(regexp = ".*\\d.*", message = "password should contain atleast one numeric")
	private String password;
	@NotBlank
	@Email(message = "Invalid email")
	private String email;

}
