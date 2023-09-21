package com.bstirbat.hotelmanagement.layeredarchitecture.utils;

import com.bstirbat.hotelmanagement.layeredarchitecture.config.user.UserDetailsImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import org.springframework.security.core.Authentication;

public class UserUtils {

  private UserUtils() {

  }

  public static User extractUser(Authentication authentication) {
    if (authentication == null) {
      return null;
    }

    return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
  }
}
