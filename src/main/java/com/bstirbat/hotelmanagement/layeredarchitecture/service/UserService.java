package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import java.util.Optional;

public interface UserService {

  User getByEmail(String email);

  User save(User user);
}
