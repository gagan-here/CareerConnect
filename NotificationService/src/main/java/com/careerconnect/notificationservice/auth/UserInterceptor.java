package com.careerconnect.notificationservice.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String userId = request.getHeader("X-User-Id");

    if (userId != null) {
      try {
        UserContextHolder.setCurrentUserId(Long.valueOf(userId));

        log.debug("Set user context from header: {}", userId);

      } catch (NumberFormatException e) {
        log.error("Invalid X-User-Id format: '{}'", userId, e);
        throw new IllegalArgumentException("Invalid user ID format", e);
      }
    }
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    log.debug("Clearing user context for user: {}", UserContextHolder.getCurrentUserId());
    UserContextHolder.clear();
  }
}
