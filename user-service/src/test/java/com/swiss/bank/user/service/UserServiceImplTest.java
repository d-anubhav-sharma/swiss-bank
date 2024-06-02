package com.swiss.bank.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import com.swiss.bank.user.service.config.CorsFilter;
import com.swiss.bank.user.service.config.DualAuthenticationManager;
import com.swiss.bank.user.service.config.EncoderConfig;
import com.swiss.bank.user.service.config.JwtRequestFilter;
import com.swiss.bank.user.service.config.MailSenderAOPConfig;
import com.swiss.bank.user.service.config.NoPopupAuthenticationEntryPoint;
import com.swiss.bank.user.service.config.WebSecurityConfig;
import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.models.UserUpdateRequest;
import com.swiss.bank.user.service.services.UserService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@ComponentScan(basePackages = {"com.swiss.bank.user.service"})
@ContextConfiguration(classes = {CorsFilter.class,
		DualAuthenticationManager.class,
		EncoderConfig.class,
		JwtRequestFilter.class,
		MailSenderAOPConfig.class,
		NoPopupAuthenticationEntryPoint.class,
		WebSecurityConfig.class})
class UserServiceImplTest {

	@Autowired
	private UserService userService;
	
	@Test
	void testGetUserAndUpdateRoles() {

		Role role = new Role();
		role.setUsername("anubhav_sharma");
		role.setRoleName("OWNER");
		role.setPrivileges(List.of("STAFF", "ADMIN"));

		Mono<User> updateUserRoles = userService.saveUserWithRoles(UserUpdateRequest
				.builder()
				.username("anubhav_sharma")
				.roles(List.of(role)).build());

		StepVerifier.create(updateUserRoles).expectSubscription().verifyComplete();

		Mono<User> updatedUser = userService.findUserByUsername("anubhav_sharma");

		StepVerifier.create(updatedUser)
				.expectNextMatches(user -> user.getRoles()
						.stream()
						.anyMatch(urole -> "OWNER".equals(urole.getRoleName()) && urole.getPrivileges().containsAll(List.of("STAFF", "ADMIN"))))
				.verifyComplete();
	}

	@Test
	void testFetchUser() {
		StepVerifier.create(userService.findUserByUsername("anubhav_sharma")).assertNext(user -> {
			assertThat(user.getRoles()).isEmpty();
		})
		.verifyComplete();
	}
}
