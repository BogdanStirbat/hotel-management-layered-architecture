package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AddressService {

  Address create(@NotNull @Valid AddressCreateDto createDto);
}
