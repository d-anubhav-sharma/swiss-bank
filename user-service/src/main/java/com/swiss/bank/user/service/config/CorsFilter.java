package com.swiss.bank.user.service.config;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Component
public class CorsFilter extends CorsWebFilter{

  public CorsFilter(CorsConfigurationSource configSource) {
    super(configSource);
  }

}
