package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.AddressRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.AddressServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.AddressGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class AddressServiceTest {

  private AddressService addressService;

  private StreetService streetService;
  private AddressRepository addressRepository;

  private Address address;
  private AddressCreateDto dto;

  @BeforeEach
  public void setUp() {
    streetService = mock(StreetService.class);
    addressRepository = mock(AddressRepository.class);
    addressService = new AddressServiceImpl(streetService, addressRepository);

    Street street = StreetGenerator.StreetBuilder.builder()
        .withId(3L)
        .withName("Street")
        .build();

    address = AddressGenerator.AddressBuilder.builder()
        .withId(4L)
        .withStreet(street)
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    dto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();
  }

  @Test
  void create() {
    // given
    when(addressRepository.save(any())).thenReturn(address);

    // when
    Address createdAddress = addressService.create(dto);

    // then
    assertEquals(address.getId(), createdAddress.getId());
    assertEquals(address.getStreet().getId(), createdAddress.getStreet().getId());
    assertEquals(address.getHouseNumber(), createdAddress.getHouseNumber());
    assertEquals(address.getPostalCode(), createdAddress.getPostalCode());
  }

  @Test
  void create_whenInvalidStreetId() {
    // given
    when(streetService.getById(3L)).thenThrow(new ResourceNotFoundException("Could not find a street with id 3"));

    // when & then
    assertThrows(InvalidDataException.class, () -> addressService.create(dto));
  }

  @Test
  void getById() {
    // given
    when(addressRepository.findById(4L)).thenReturn(Optional.of(address));

    // when
    Address retrievedAddress = addressService.getById(4L);

    // then
    assertEquals(address.getId(), retrievedAddress.getId());
    assertEquals(address.getStreet().getId(), retrievedAddress.getStreet().getId());
    assertEquals(address.getHouseNumber(), retrievedAddress.getHouseNumber());
    assertEquals(address.getPostalCode(), retrievedAddress.getPostalCode());
  }

  @Test
  void getById_whenNoAddressFound() {
    // given
    when(addressRepository.findById(1L)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ResourceNotFoundException.class, () -> addressService.getById(1L));
  }

  @Test
  void findAll() {
    // given
    Page<Address> page = Page.empty();

    when(addressRepository.findAll(any(Pageable.class))).thenReturn(page);

    // when
    Page<Address> addresses = addressService.findAll(Pageable.unpaged());

    // then
    assertTrue(addresses.isEmpty());
  }
}
