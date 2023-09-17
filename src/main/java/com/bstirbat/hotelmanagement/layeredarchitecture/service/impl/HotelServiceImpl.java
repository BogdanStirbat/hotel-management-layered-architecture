package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.ExceptionWrapperUtils.wrapWithInvalidDataException;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.HotelMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.HotelRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class HotelServiceImpl implements HotelService {

  private final AddressService addressService;
  private final HotelRepository hotelRepository;

  @Autowired
  public HotelServiceImpl(AddressService addressService, HotelRepository hotelRepository) {
    this.addressService = addressService;
    this.hotelRepository = hotelRepository;
  }

  @Override
  public Hotel create(@NotNull @Valid HotelCreateDto createDto) {
    Hotel hotel = HotelMapper.INSTANCE.toEntity(createDto);
    hotel.setAddress(wrapWithInvalidDataException(() -> addressService.getById(createDto.getAddressId())));

    return hotelRepository.save(hotel);
  }

  @Override
  public Hotel getById(@NotNull Long id) {

    return hotelRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find hotel with id %s", id)));
  }
}
