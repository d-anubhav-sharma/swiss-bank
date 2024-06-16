package com.swiss.bank.payment.service.config;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class NoPopupAuthenticationEntryPoint implements ServerAuthenticationEntryPoint{

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
		exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
		return exchange.getResponse().setComplete();
	}

	

}
