package com.swiss.bank.user.service.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.exceptions.InvalidUsernamePasswordException;
import com.swiss.bank.user.service.services.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class DualAuthenticationManager implements ReactiveAuthenticationManager{

	PasswordEncoder passwordEncoder;
	
	UserService userService;
	
	DualAuthenticationManager(PasswordEncoder passwordEncoder, UserService userService){
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}
	
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		log.atInfo().log("Username: {} Password: {}", username, password);
		Mono<User> userMono = userService
				.findUserByUsername(username)
				.switchIfEmpty(Mono.error(new InvalidUsernamePasswordException("No such user exists")))
				.map(user -> {
					if(!passwordEncoder.matches(password, user.getPassword())) {
						throw new InvalidUsernamePasswordException("for user: "+user.getUsername());
					}
					return user;
				});

		return userService
				.findAuthoritiesForUserName(username)
				.zipWith(userMono, (authorities, user) -> 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities));
	}

}
