package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface HotelService {

  Hotel create(@NotNull @Valid HotelCreateDto createDto);

  Hotel getById(@NotNull Long id);
}
