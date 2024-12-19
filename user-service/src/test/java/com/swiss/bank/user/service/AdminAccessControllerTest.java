package com.swiss.bank.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiss.bank.user.service.models.UserAccessResponse;

@SpringBootTest
class AdminAccessControllerTest {

	int port;
	WebTestClient webTestClient = WebClient.create();
	
	@Test
	void testFindAllUsers() {
		System.out.println(port);
		webTestClient
			.get()
			.uri("http://localhost:10001/user-service/users")
			.retrieve()
			.bodyToFlux(UserAccessResponse.class)
			.collectList()
			.subscribe();
	}
	
}
