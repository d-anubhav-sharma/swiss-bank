package com.swiss.bank.user.service.services.impl;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.swiss.bank.user.service.exceptions.InvalidUsernamePasswordException;
import com.swiss.bank.user.service.models.LoginRequest;
import com.swiss.bank.user.service.models.LoginResponse;
import com.swiss.bank.user.service.models.LogoutResponse;
import com.swiss.bank.user.service.models.RegisterUserRequest;
import com.swiss.bank.user.service.models.RegisterUserResponse;
import com.swiss.bank.user.service.services.AuthenticationService;
import com.swiss.bank.user.service.services.UserService;
import com.swiss.bank.user.service.util.DataUtil;
import com.swiss.bank.user.service.util.JwtTokenUtil;
import com.swiss.bank.user.service.util.SwissConstants;

import reactor.core.publisher.Mono;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	ReactiveAuthenticationManager authenticationManager;

	JwtTokenUtil jwtTokenUtil;
	
	UserService userService;
	
	PasswordEncoder encoder;
	
	AuthenticationServiceImpl(
			ReactiveAuthenticationManager authenticationManager, 
			JwtTokenUtil jwtTokenUtil,
			UserService userService,
			PasswordEncoder encoder){
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
		this.encoder = encoder;
	}

	@Override
	public Mono<LoginResponse> login(LoginRequest loginRequest, ServerWebExchange exchange) {
		return userService
			.findUserByUsername(loginRequest.getUsername())
			.switchIfEmpty(Mono.error(new InvalidUsernamePasswordException("Invalida username/password exception")))
			.flatMap(user -> 
				authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(),
						loginRequest.getPassword(), 
						DataUtil.getGrantedAuthoritiesFromRoles(user.getRoles())))
			)
			.doOnNext(auth -> {
				exchange.getResponse().addCookie(ResponseCookie
						.from("auth_token",jwtTokenUtil.generateAuthToken(auth.getPrincipal().toString()))
						.httpOnly(true)
						.secure(true)
						.path("/")
						.maxAge(Duration.ofHours(1))
						.build());
				exchange.getResponse().addCookie(ResponseCookie
						.from(SwissConstants.USERNAME,auth.getPrincipal().toString())
						.httpOnly(true)
						.secure(true)
						.path("/")
						.maxAge(Duration.ofHours(1))
						.build());				
			})
			.map(auth -> LoginResponse
							.builder()
							.build());

	}

	@Override
	public Mono<RegisterUserResponse> register(RegisterUserRequest userRegistrationRequest) {
		return userService
				.registerUser(userRegistrationRequest);
	}

	@Override
	public Mono<LogoutResponse> logout(ServerWebExchange exchange) {
		exchange.getResponse().addCookie(ResponseCookie
				.from("auth_token","")
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(Duration.ofSeconds(0))
				.build());
		exchange.getResponse().addCookie(ResponseCookie
				.from(SwissConstants.USERNAME,"")
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(Duration.ofSeconds(0))
				.build());
		return Mono.just(LogoutResponse.builder().message("success").build());
	}

}
