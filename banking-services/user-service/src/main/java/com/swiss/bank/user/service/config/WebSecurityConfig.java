package com.swiss.bank.user.service.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.swiss.bank.user.service.util.SwissConstants;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

	private static final List<String> ALLOWED_ORIGINS = Collections.unmodifiableList(
			List.of("http://localhost:10000", "http://localhost:10001", "http://localhost:10002", "http://localhost:10003", "http://localhost:10004",
					"http://localhost:10005", "http://localhost:10006", "http://localhost:10007", "https://www.google.com", "http://localhost:3000"));
	private static final List<String> ALLOWED_METHODS = Collections.unmodifiableList(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

	private JwtRequestFilter jwtRequestFilter;

	public WebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(ALLOWED_ORIGINS);
		configuration.setAllowedMethods(ALLOWED_METHODS);
		configuration.setAllowCredentials(true);
		configuration.addAllowedHeader("*");
		configuration.addExposedHeader(SwissConstants.USERNAME);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
		return serverHttpSecurity
				.csrf(CsrfSpec::disable)
				.httpBasic(http -> http.authenticationEntryPoint(new NoPopupAuthenticationEntryPoint()))
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(exchange -> exchange
						.pathMatchers(
								"/auth/**", 
								"/webjars/swagger-ui/index.html", 
								"/webjars/swagger-ui/swagger-ui.css",
								"/webjars/swagger-ui/index.css",
								"/webjars/swagger-ui/swagger-ui-bundle.js",
								"/webjars/swagger-ui/swagger-ui-standalone-preset.js",
								"/webjars/swagger-ui/swagger-initializer.js",
								"/v3/api-docs/swagger-config",
								"/webjars/swagger-ui/favicon-32x32.png",
								"/v3/api-docs").permitAll()
						.pathMatchers(HttpMethod.OPTIONS, "**").permitAll()
						.pathMatchers("/admin/**", "/user/**").hasAuthority("STAFF")
						.anyExchange().authenticated())
				.addFilterBefore(jwtRequestFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.formLogin(FormLoginSpec::disable)
				.build();
	}
}
