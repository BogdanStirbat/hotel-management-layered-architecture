package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AuthenticationRequest;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.SignUpRequest;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AuthenticationResponse;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentications")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @Autowired
  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(authenticationService.login(request));
  }

  @PostMapping("/signup")
  public ResponseEntity<AuthenticationResponse> signup(@RequestBody SignUpRequest request) {
    return ResponseEntity.ok(authenticationService.signup(request));
  }
}
