package com.careerconnect.ApiGateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFilter
    extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  public AuthenticationFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      log.info("Login request: {}", exchange.getRequest().getURI());

      final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

      if (tokenHeader == null || !tokenHeader.startsWith("Bearer")) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        log.error("Authorization token header not found");
        return exchange.getResponse().setComplete();
      }

      final String token = tokenHeader.split("Bearer")[1];

      return chain.filter(exchange);
    };
  }

  public static class Config {}
}
