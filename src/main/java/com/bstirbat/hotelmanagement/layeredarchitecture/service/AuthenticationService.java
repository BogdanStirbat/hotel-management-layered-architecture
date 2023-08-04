package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AuthenticationRequest;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.SignUpRequest;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AuthenticationResponse;

public interface AuthenticationService {

  AuthenticationResponse signup(SignUpRequest signUpRequest);

  AuthenticationResponse signup(AuthenticationRequest authenticationRequest);

  AuthenticationResponse login(AuthenticationRequest authenticationRequest);
}
