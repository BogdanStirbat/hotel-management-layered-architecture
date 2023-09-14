package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.ExceptionWrapperUtils.wrapWithInvalidDataException;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.AddressMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.AddressRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AddressServiceImpl implements AddressService {

  private final StreetService streetService;
  private final AddressRepository addressRepository;

  @Autowired
  public AddressServiceImpl(StreetService streetService, AddressRepository addressRepository) {
    this.streetService = streetService;
    this.addressRepository = addressRepository;
  }

  @Override
  public Address create(@NotNull @Valid AddressCreateDto createDto) {
    Address address = AddressMapper.INSTANCE.toEntity(createDto);
    address.setStreet(wrapWithInvalidDataException(() -> streetService.getById(createDto.getStreetId())));

    return addressRepository.save(address);
  }
}
