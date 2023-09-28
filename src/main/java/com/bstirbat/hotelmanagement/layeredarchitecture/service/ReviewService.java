package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ReviewService {

  Review create(@NotNull @Valid ReviewCreateDto createDto, @NotNull User user);

  Review getById(@NotNull Long id);
}
