package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CountryService {

  Country getById(@NotNull Long id);

  Country create(@NotNull @Valid CountryCreateDto createDto);
}
