package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

@Validated
public interface RoomTypeService {

  RoomType create(@NotNull @Valid RoomTypeCreateDto createDto);

  RoomType getById(@NotNull Long id);

  Page<RoomType> findAll(@NotNull Pageable pageable);
}
