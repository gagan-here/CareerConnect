package com.careerconnect.userservice.service;

import com.careerconnect.userservice.dto.SignupRequestDto;
import com.careerconnect.userservice.dto.UserDto;
import com.careerconnect.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private UserRepository userRepository;

  public UserDto signUp(SignupRequestDto signupRequestDto) {
    return null;
  }
}
