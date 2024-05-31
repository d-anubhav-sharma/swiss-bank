package com.swiss.bank.user.service.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.swiss.bank.user.service.definitions.Gender;
import com.swiss.bank.user.service.definitions.Nationality;

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
public class BasicInfo {

	@Id
	private String id;
	@Nonnull
	private String username;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private Date dateofBirth;
	private int age;
	private Gender gender;
	private String phone;
	private String secondaryEmail;
	private String secondaryPhone;
	private Nationality nationality;
	private String profilePic;
	
}
