package com.swiss.bank.payment.service.config;

import java.net.URI;

import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.swiss.bank.common.exceptions.InvalidUsernamePasswordException;
import com.swiss.bank.common.models.GetUserFromTokenRequest;
import com.swiss.bank.common.models.User;
import com.swiss.bank.common.utils.DataUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtRequestFilter implements WebFilter {

	private WebClient webClient;
	
	public JwtRequestFilter(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		HttpCookie authCookie = exchange.getRequest().getCookies().getFirst("auth_token");
		HttpCookie usernameCookie = exchange.getRequest().getCookies().getFirst("username");
		if (authCookie == null || usernameCookie == null) {
			log.atInfo().log("cookie not found for authentication or username");
			return chain.filter(exchange);
		}
		return webClient.post().uri(URI.create("http://localhost:10001/user-service/auth/fetchUserUsingAuthToken"))
			.cookie("auth_token", authCookie.getValue())
			.cookie("username", usernameCookie.getValue())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(GetUserFromTokenRequest.builder()
					.authToken(authCookie.getValue())
					.username(usernameCookie.getValue())
					.build())
			.retrieve()
			.bodyToMono(User.class)
			.switchIfEmpty(Mono.error(new InvalidUsernamePasswordException()))
			.flatMap(user -> {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						user.getUsername(), 
						user.getUsername(),
						DataUtil.getGrantedAuthoritiesFromRoles(user.getRoles()));
				SecurityContext securityContext = new SecurityContextImpl(authentication);
				return chain
						.filter(exchange)
						.contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
			});

	}


}
