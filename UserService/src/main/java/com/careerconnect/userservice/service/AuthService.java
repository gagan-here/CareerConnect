package com.careerconnect.userservice.service;

import com.careerconnect.userservice.dto.LoginRequestDto;
import com.careerconnect.userservice.dto.SignupRequestDto;
import com.careerconnect.userservice.dto.UserDto;
import com.careerconnect.userservice.entity.User;
import com.careerconnect.userservice.exception.BadRequestException;
import com.careerconnect.userservice.exception.ResourceNotFoundException;
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
  private final UserRepository userRepository;
  private final JwtService jwtService;

  public UserDto signUp(SignupRequestDto signupRequestDto) {

    boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
    if (exists) {
      throw new BadRequestException("User already exists, cannot signup again.");
    }

    User user = modelMapper.map(signupRequestDto, User.class);
    user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

    User savedUser = userRepository.save(user);
    return modelMapper.map(savedUser, UserDto.class);
  }

  public String login(LoginRequestDto loginRequestDto) {
    User user =
        userRepository
            .findByEmail(loginRequestDto.getEmail())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "User not found with email: " + loginRequestDto.getEmail()));

    boolean isPasswordMatch =
        PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

    if (!isPasswordMatch) {
      throw new BadRequestException("Incorrect Password!");
    }

    return jwtService.generateAccessToken(user);
  }
}
