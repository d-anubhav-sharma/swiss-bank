package com.swiss.bank.user.service.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

import com.swiss.bank.common.utils.DataUtil;
import com.swiss.bank.user.service.repositories.PathPrivilegeMapperRepository;
import com.swiss.bank.user.service.util.SwissConstants;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Slf4j
public class WebSecurityConfig {

	private static final List<String> ALLOWED_METHODS = Collections
			.unmodifiableList(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

	private JwtRequestFilter jwtRequestFilter;
	private List<String> allowedOrigins;
	private PathPrivilegeMapperRepository pathPrivilegeMapperRepository;

	public WebSecurityConfig(
			JwtRequestFilter jwtRequestFilter, 
			@Value("${allowed-origins}") String allowedHosts,
			PathPrivilegeMapperRepository pathPrivilegeMapperRepository) {
		this.jwtRequestFilter = jwtRequestFilter;
		this.allowedOrigins = Collections.unmodifiableList(Arrays.asList(allowedHosts.split(",")));
		this.pathPrivilegeMapperRepository = pathPrivilegeMapperRepository;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(allowedOrigins);
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
		Customizer<AuthorizeExchangeSpec> httpRequestCustomizer = exchange -> {
			AuthorizeExchangeSpec permission = exchange
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
							"/v3/api-docs",
							"/actuator/**",
							"/actuator/health")
					.permitAll();
			pathPrivilegeMapperRepository
				.findAllByCategory("user-service")
				.doOnNext(pathPrivilegeMap -> {
					HttpMethod method = DataUtil.getMethod(pathPrivilegeMap.getMethod());
					if(method!=null) {
						log.atInfo().log("Securing url: {} and method: {} with privilege: {}",
								pathPrivilegeMap.getUrlPattern(),
								method,
								pathPrivilegeMap.getPrivilegeName());
						permission
							.pathMatchers(method, pathPrivilegeMap.getUrlPattern())
							.hasAuthority(pathPrivilegeMap.getPrivilegeName());
					}
					else {
						log.atInfo().log("Securing url: {} with privilege: {}",
								pathPrivilegeMap.getUrlPattern(),
								pathPrivilegeMap.getPrivilegeName());
						permission
							.pathMatchers(pathPrivilegeMap.getUrlPattern())
							.hasAuthority(pathPrivilegeMap.getPrivilegeName());
					}
				})
				.doFinally(signal -> permission.anyExchange().authenticated())
				.subscribe();
		};
		
		return serverHttpSecurity
				.csrf(CsrfSpec::disable)
				.httpBasic(http -> http.authenticationEntryPoint(new NoPopupAuthenticationEntryPoint()))
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(httpRequestCustomizer)
				.addFilterBefore(jwtRequestFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.formLogin(FormLoginSpec::disable)
				.build();
	}
}
