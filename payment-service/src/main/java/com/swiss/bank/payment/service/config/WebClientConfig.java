package com.swiss.bank.payment.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
public class WebClientConfig {


	@Bean
	WebClient webClient() {
		return WebClient.create();
	}
	
	@Primary
	@Bean
	Gson gson() {
		return new GsonBuilder().setPrettyPrinting().create();
	}
}
