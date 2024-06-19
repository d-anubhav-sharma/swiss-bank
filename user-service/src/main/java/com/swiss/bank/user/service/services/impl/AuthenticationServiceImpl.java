package com.swiss.bank.user.service.services.impl;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.exceptions.InvalidUsernamePasswordException;
import com.swiss.bank.user.service.models.GetUserFromTokenRequest;
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

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
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
			.map(auth -> {
				log.atInfo().log("Generating and appending cookie: {}", auth.getPrincipal());
				String username = auth.getPrincipal().toString();
				String authToken = jwtTokenUtil.generateAuthToken(auth.getPrincipal().toString());
				exchange.getResponse().addCookie(ResponseCookie
						.from("auth_token",authToken)
						.httpOnly(true)
						.domain("localhost")
						.secure(true)
						.path("/")
						.maxAge(Duration.ofHours(1))
						.build());
				exchange.getResponse().addCookie(ResponseCookie
						.from(SwissConstants.USERNAME,username)
						.httpOnly(true)
						.domain("localhost")
						.secure(true)
						.path("/")
						.maxAge(Duration.ofHours(1))
						.build());
				return LoginResponse
						.builder()
						.username(username)
						.token(authToken)
						.build();
			});

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

	@Override
	public Mono<User> getUserFromToken(GetUserFromTokenRequest getUserFromTokenRequest) {
		String authToken = getUserFromTokenRequest.getAuthToken();
		Claims claims = jwtTokenUtil.getClaimsFromAuthToken(authToken);
		String username = claims.get(SwissConstants.USERNAME).toString();
		if(jwtTokenUtil.validateToken(authToken) && username!=null && username.equals(getUserFromTokenRequest.getUsername())) {
			return userService.findUserByUsername(username);
		}
		return Mono.empty();
		
	}

}
