package com.swiss.bank.common.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiss.bank.common.models.LoginRequest;
import com.swiss.bank.common.models.LoginResponse;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

	private String userServiceBaseUrl;

	WebClientConfig(@Value("${user-service.base-url}") String userServiceBaseUrl) {
		this.userServiceBaseUrl = userServiceBaseUrl;
	}

	@Bean
	@Primary
	WebClient webClient() {
		return WebClient.create();
	}

	@Bean
	Mono<LoginResponse> userServiceLoginResponse() {
		return webClient().post().uri(URI.create(userServiceBaseUrl + "/auth/login"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.bodyValue(LoginRequest.builder().username("anubhav_sharma").password("Anubhav@123").build()).retrieve()
				.bodyToMono(LoginResponse.class);
	}

	@Bean("authenticatedWebclient")
	WebClient authenticatedWebclient(Mono<LoginResponse> loginResponseMono) {
		return loginResponseMono.map(loginResponse -> WebClient.builder().defaultCookies(cookies -> {
			cookies.add("auth_token", loginResponse.getToken());
			cookies.add("username", loginResponse.getUsername());
		}).build()).block();
	}

}
