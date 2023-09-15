package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.StreetDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.AddressGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator.StreetBuilder;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator.StreetCreateDtoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

class AddressControllerTest {

  private AddressService addressService;
  private AddressController addressController;

  private Address address;

  private AddressCreateDto createDto;

  @BeforeEach
  public void setUp() {
    addressService = mock(AddressService.class);
    addressController = new AddressController(addressService);

    Street street = StreetBuilder.builder()
        .withId(3L)
        .withName("Street")
        .build();

    address = AddressGenerator.AddressBuilder.builder()
        .withId(4L)
        .withStreet(street)
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();
  }

  @Test
  void create() {
    // given
    when(addressService.create(any())).thenReturn(address);

    // when
    ResponseEntity<AddressDto> responseEntity = addressController.create(createDto);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    AddressDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(address.getId(), responseDto.getId());
    assertEquals(address.getStreet().getId(), responseDto.getStreetId());
    assertEquals(address.getHouseNumber(), responseDto.getHouseNumber());
    assertEquals(address.getPostalCode(), responseDto.getPostalCode());
  }

  @Test
  void getById() {
    // given
    when(addressService.getById(4L)).thenReturn(address);

    // when
    ResponseEntity<AddressDto> responseEntity = addressController.getById(4L);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    AddressDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(address.getId(), responseDto.getId());
    assertEquals(address.getStreet().getId(), responseDto.getStreetId());
    assertEquals(address.getHouseNumber(), responseDto.getHouseNumber());
  }

  @Test
  void findAll() {
    // when
    when(addressService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<AddressDto>> responseEntity = addressController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
