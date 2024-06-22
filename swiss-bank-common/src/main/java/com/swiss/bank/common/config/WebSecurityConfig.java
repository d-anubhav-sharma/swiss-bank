package com.swiss.bank.common.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

	private static final List<String> ALLOWED_METHODS = Collections
			.unmodifiableList(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

	
	private JwtRequestFilter jwtRequestFilter;
	private List<String> allowedOrigins;
	private String userServiceBaseUrl;

	public WebSecurityConfig(JwtRequestFilter jwtRequestFilter, @Value("${allowed-origins}") String allowedHosts, @Value("${user-service.base-url}") String userServiceBaseUrl) {
		this.jwtRequestFilter = jwtRequestFilter;
		this.allowedOrigins = Collections.unmodifiableList(Arrays.asList(allowedHosts.split(",")));
		this.userServiceBaseUrl = userServiceBaseUrl;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(allowedOrigins);
		configuration.setAllowedMethods(ALLOWED_METHODS);
		configuration.setAllowCredentials(true);
		configuration.addAllowedHeader("*");
		configuration.addExposedHeader("username");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity, @Qualifier("authenticatedWebclient") WebClient webClient) {

		Customizer<AuthorizeExchangeSpec> authorizeExchangeSpec = exchange -> exchange
				.pathMatchers("/auth/**", "/webjars/swagger-ui/index.html",
						"/webjars/swagger-ui/swagger-ui.css", "/webjars/swagger-ui/index.css",
						"/webjars/swagger-ui/swagger-ui-bundle.js",
						"/webjars/swagger-ui/swagger-ui-standalone-preset.js",
						"/webjars/swagger-ui/swagger-initializer.js", "/v3/api-docs/swagger-config",
						"/webjars/swagger-ui/favicon-32x32.png", "/test/giveAdminAccessToOwner", "/v3/api-docs").permitAll();
		
//		webClient
//			.get()
//			.uri(userServiceBaseUrl+"/access/roles/all")
//			.retrieve()
//			.bodyToFlux(String.class)
//			.doOnNext(url -> authorizeExchangeSpec);
		
		return serverHttpSecurity.csrf(CsrfSpec::disable)
				.httpBasic(http -> http.authenticationEntryPoint(new NoPopupAuthenticationEntryPoint()))
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(exchange -> exchange
						.pathMatchers("/auth/**", "/webjars/swagger-ui/index.html",
								"/webjars/swagger-ui/swagger-ui.css", "/webjars/swagger-ui/index.css",
								"/webjars/swagger-ui/swagger-ui-bundle.js",
								"/webjars/swagger-ui/swagger-ui-standalone-preset.js",
								"/webjars/swagger-ui/swagger-initializer.js", "/v3/api-docs/swagger-config",
								"/webjars/swagger-ui/favicon-32x32.png", "/test/giveAdminAccessToOwner", "/v3/api-docs")
						.permitAll().pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.pathMatchers("/admin/**", "/user/**").hasAuthority("STAFF")
						.pathMatchers("/approval/**").hasAuthority("ADMIN")
						.anyExchange().authenticated())
				.addFilterBefore(jwtRequestFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.formLogin(FormLoginSpec::disable).build();
	}
}
