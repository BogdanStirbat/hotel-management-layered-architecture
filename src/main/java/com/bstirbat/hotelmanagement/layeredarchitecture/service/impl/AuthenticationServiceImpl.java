package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AuthenticationRequest;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.SignUpRequest;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AuthenticationResponse;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Role;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AuthenticationService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.JwtService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthenticationServiceImpl(
      UserService userService,
      PasswordEncoder passwordEncoder,
      JwtService jwtService,
      AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public AuthenticationResponse signup(SignUpRequest request) {
    User user = new User();
    user.setRole(request.getRole());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    User savedUser = userService.save(user);
    String token = jwtService.generateToken(savedUser.getEmail());

    return new AuthenticationResponse(token);
  }

  @Override
  public AuthenticationResponse signup(AuthenticationRequest request) {
    User user = new User();
    user.setRole(Role.CLIENT);
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    User savedUser = userService.save(user);
    String token = jwtService.generateToken(savedUser.getEmail());

    return new AuthenticationResponse(token);
  }

  @Override
  public AuthenticationResponse login(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = userService.getByEmail(request.getEmail());
    String token = jwtService.generateToken(user.getEmail());

    return new AuthenticationResponse(token);
  }
}
