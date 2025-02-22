package com.careerconnect.ApiGateway.filters;

import com.careerconnect.ApiGateway.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class AuthenticationFilter
    extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  private final JwtService jwtService;

  public AuthenticationFilter(JwtService jwtService) {
    super(Config.class);
    this.jwtService = jwtService;
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

      String userId = jwtService.getUserIdFromToken(token);

      ServerWebExchange modifedExchange =
          exchange.mutate().request(r -> r.header("X-User-Id", userId)).build();

      return chain.filter(modifedExchange);
    };
  }

  public static class Config {}
}
