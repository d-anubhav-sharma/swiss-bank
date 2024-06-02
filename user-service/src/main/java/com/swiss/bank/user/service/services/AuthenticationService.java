package com.swiss.bank.user.service.services;

import org.springframework.web.server.ServerWebExchange;

import com.swiss.bank.user.service.models.LoginRequest;
import com.swiss.bank.user.service.models.LoginResponse;
import com.swiss.bank.user.service.models.LogoutResponse;
import com.swiss.bank.user.service.models.RegisterUserRequest;
import com.swiss.bank.user.service.models.RegisterUserResponse;

import reactor.core.publisher.Mono;

public interface AuthenticationService {

	public Mono<LoginResponse> login(LoginRequest loginRequest, ServerWebExchange exchange);

	public Mono<RegisterUserResponse> register(RegisterUserRequest userRegistrationRequest);

	public Mono<LogoutResponse> logout(ServerWebExchange exchange);

}
