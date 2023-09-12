package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface StreetService {

  Street create(@NotNull @Valid StreetCreateDto createDto);
}
