package com.swiss.bank.user.service.config;

import java.util.List;
import java.util.function.Function;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.services.UserService;
import com.swiss.bank.user.service.util.JwtTokenUtil;
import com.swiss.bank.user.service.util.SwissConstants;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtRequestFilter implements WebFilter {

	UserService userService;

	JwtTokenUtil jwtTokenUtil;
	
	public JwtRequestFilter(UserService userService, JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		HttpCookie authCookie = exchange.getRequest().getCookies().getFirst("auth_token");
		HttpCookie usernameCookie = exchange.getRequest().getCookies().getFirst(SwissConstants.USERNAME);
		if (authCookie == null || usernameCookie == null) {
			log.atInfo().log("cookie not found for authentication or username");
			return chain.filter(exchange);
		}

		String username = usernameCookie.getValue();
		String authToken = authCookie.getValue();

		if (username == null || !jwtTokenUtil.validateToken(authToken)) {
			log.atInfo().log("Username null or authentication failed: {}", username);
			return chain.filter(exchange);
		}
		log.atInfo().log("Authentication successful. User should get the access: {}", username);
		addUsernameToResponseHeader(exchange, username);
		Mono<User> userMono = userService.findUserByUsername(username);
		Mono<List<SimpleGrantedAuthority>> authoritiesMono = userService.findAuthoritiesForUserName(username);
		return userMono
				.zipWith(authoritiesMono, (user, authorities) -> {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					user.getUsername(), 
					user.getPassword(),
					authorities);
			SecurityContext securityContext = new SecurityContextImpl(authentication);
			return chain
					.filter(exchange)
					.contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
		})
		.flatMap(Function.identity());
	}

	private void addUsernameToResponseHeader(ServerWebExchange exchange, String username) {
		HttpHeaders headers = exchange.getResponse().getHeaders();
		headers.set(SwissConstants.USERNAME, username);
	}

}
