package com.swiss.bank.account.service.config;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class NoPopupAuthenticationEntryPoint implements ServerAuthenticationEntryPoint{

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
		log.atInfo().log("Commence method: {}", ex.getMessage());
		exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
		return exchange.getResponse().setComplete();
	}

	

}
