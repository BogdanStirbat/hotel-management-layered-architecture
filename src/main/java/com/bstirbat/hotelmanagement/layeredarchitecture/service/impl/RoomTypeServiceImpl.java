package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.ExceptionWrapperUtils.wrapWithInvalidDataException;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.RoomTypeMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.RoomTypeRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.HotelService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.RoomTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class RoomTypeServiceImpl implements RoomTypeService {

  private final HotelService hotelService;
  private final RoomTypeRepository roomTypeRepository;

  @Autowired
  public RoomTypeServiceImpl(HotelService hotelService, RoomTypeRepository roomTypeRepository) {
    this.hotelService = hotelService;
    this.roomTypeRepository = roomTypeRepository;
  }

  @Override
  public RoomType create(@NotNull @Valid RoomTypeCreateDto createDto) {
    RoomType roomType = RoomTypeMapper.INSTANCE.toEntity(createDto);
    roomType.setHotel(wrapWithInvalidDataException(() -> hotelService.getById(createDto.getHotelId())));

    return roomTypeRepository.save(roomType);
  }

  @Override
  public RoomType getById(@NotNull Long id) {

    return roomTypeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find room type with id %s", id)));
  }

  @Override
  public Page<RoomType> findAll(@NotNull Pageable pageable) {

    return roomTypeRepository.findAll(pageable);
  }
}
