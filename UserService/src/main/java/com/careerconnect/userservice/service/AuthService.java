package com.careerconnect.userservice.service;

import com.careerconnect.userservice.dto.SignupRequestDto;
import com.careerconnect.userservice.dto.UserDto;
import com.careerconnect.userservice.entity.User;
import com.careerconnect.userservice.repository.UserRepository;
import com.careerconnect.userservice.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final ModelMapper modelMapper;
  private UserRepository userRepository;

  public UserDto signUp(SignupRequestDto signupRequestDto) {
    User user = modelMapper.map(signupRequestDto, User.class);
    user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

    User savedUser = userRepository.save(user);
    return modelMapper.map(savedUser, UserDto.class);
  }
}
