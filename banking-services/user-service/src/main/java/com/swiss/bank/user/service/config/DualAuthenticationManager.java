package com.swiss.bank.user.service.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.swiss.bank.user.service.exceptions.InvalidUsernamePasswordException;
import com.swiss.bank.user.service.services.UserService;
import com.swiss.bank.user.service.util.DataUtil;

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
		
		return userService
					.findUserByUsername(username)
					.switchIfEmpty(Mono.error(new InvalidUsernamePasswordException("Invalida username/password exception")))
					.map(user -> {
						log.atInfo().log("user found with given username: {}", username);
						if(!passwordEncoder.matches(password, user.getPassword())) {
							throw new InvalidUsernamePasswordException("for user: "+user.getUsername());
						}
						log.atInfo().log("Password matched for user: {}", username);
						return new UsernamePasswordAuthenticationToken(
								user.getUsername(), 
								user.getPassword(),
								DataUtil.getGrantedAuthoritiesFromRoles(user.getRoles()));
					});
	}

}
