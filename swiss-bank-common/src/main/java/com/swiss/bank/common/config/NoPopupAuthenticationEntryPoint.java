package com.swiss.bank.common.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;

import com.swiss.bank.common.exceptions.InvalidUsernamePasswordException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class NoPopupAuthenticationEntryPoint implements ServerAuthenticationEntryPoint{

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
		log.atInfo().log("Commence method: {}", ex.getMessage());
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return exchange
				.getResponse()
				.setComplete()
				.then(Mono.error(new InvalidUsernamePasswordException("User unauthorized")));
	}

}
