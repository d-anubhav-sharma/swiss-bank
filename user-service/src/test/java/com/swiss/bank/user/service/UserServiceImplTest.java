package com.swiss.bank.user.service;

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
import com.swiss.bank.user.service.repositories.UserRepository;
import com.swiss.bank.user.service.services.UserService;

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
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	void testGetUserAndUpdateRoles() {
		userRepository.deleteAll().subscribe();
	}

	@Test
	void testFetchUser() {
		StepVerifier.create(userService.findUserByUsername("anubhav_sharma")).assertNext(user -> {
//			assertThat(user.getRoles()).isEmpty();
		})
		.verifyComplete();
	}
}
