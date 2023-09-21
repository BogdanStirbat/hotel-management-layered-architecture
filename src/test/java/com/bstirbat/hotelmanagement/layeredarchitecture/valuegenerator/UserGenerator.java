package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.enums.Role;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;

public class UserGenerator {

  public static class UserBuilder {
    private Long id;
    private String email;
    private Role role;

    private UserBuilder() {

    }

    public static UserGenerator.UserBuilder builder() {
      return new UserGenerator.UserBuilder();
    }

    public UserGenerator.UserBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public UserGenerator.UserBuilder withEmail(String email) {
      this.email = email;
      return this;
    }

    public UserGenerator.UserBuilder withRole(Role role) {
      this.role = role;
      return this;
    }

    public User build() {
      User booking = new User();
      booking.setId(id);
      booking.setEmail(email);
      booking.setRole(role);

      return booking;
    }
  }
}
