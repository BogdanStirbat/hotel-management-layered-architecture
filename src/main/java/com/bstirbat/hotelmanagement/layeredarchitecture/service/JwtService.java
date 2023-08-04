package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String extractUserName(String token);

  String generateToken(String email);

  boolean isTokenValid(String token, UserDetails userDetails);
}
