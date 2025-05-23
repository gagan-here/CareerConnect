package com.careerconnect.postsservice.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInteceptor implements RequestInterceptor {
  @Override
  public void apply(RequestTemplate requestTemplate) {
    Long userId = UserContextHolder.getCurrentUserId();
    if (userId != null) {
      requestTemplate.header("X-User-Id", userId.toString());
    }
  }
}
