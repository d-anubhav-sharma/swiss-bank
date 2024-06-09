package com.swiss.bank.account.service.config;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CorsFilter extends CorsWebFilter{

  public CorsFilter(CorsConfigurationSource configSource) {
    super(configSource);
    log.atInfo().log("Inside cors filter");
  }

}
