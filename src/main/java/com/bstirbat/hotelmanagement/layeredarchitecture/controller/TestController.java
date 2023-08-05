package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import com.bstirbat.hotelmanagement.layeredarchitecture.config.user.UserDetailsImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/test")
  public ResponseEntity<String> testGet(Authentication authentication) {
    User user = extractUser(authentication);
    return ResponseEntity.ok("test GET");
  }

  @PostMapping("/test")
  public ResponseEntity<String> testPost(Authentication authentication) {
    User user = extractUser(authentication);
    return ResponseEntity.ok("test POST");
  }

  private User extractUser(Authentication authentication) {
    if (authentication == null) {
      return null;
    }
    return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
  }
}
