package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/test")
  public ResponseEntity<String> test(Authentication authentication) {
    return ResponseEntity.ok("test");
  }
}
