package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;

public interface UserService {

  User getByEmail(String email);

  User save(User user);
}
