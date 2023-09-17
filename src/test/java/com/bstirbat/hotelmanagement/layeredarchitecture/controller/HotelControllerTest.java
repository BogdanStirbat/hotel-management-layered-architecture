package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.HotelDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.HotelService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.AddressGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

class HotelControllerTest {

  private HotelService hotelService;
  private HotelController hotelController;

  private Address address;

  @BeforeEach
  public void setUp() {
    hotelService = mock(HotelService.class);
    hotelController = new HotelController(hotelService);

    address = AddressGenerator.AddressBuilder.builder()
        .withId(4L)
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();
  }

  @Test
  void create() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    Hotel hotel = HotelGenerator.HotelBuilder.builder()
        .withId(5L)
        .withAddress(address)
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    when(hotelService.create(any())).thenReturn(hotel);

    // when
    ResponseEntity<HotelDto> responseEntity = hotelController.create(createDto);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    HotelDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(hotel.getId(), responseDto.getId());
    assertEquals(hotel.getAddress().getId(), responseDto.getAddressId());
    assertEquals(hotel.getName(), responseDto.getName());
    assertEquals(hotel.getDescription(), responseDto.getDescription());
  }

  @Test
  void getById() {
    // given
    Hotel hotel = HotelGenerator.HotelBuilder.builder()
        .withId(5L)
        .withAddress(address)
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    when(hotelService.getById(hotel.getId())).thenReturn(hotel);

    // when
    ResponseEntity<HotelDto> responseEntity = hotelController.getById(hotel.getId());

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    HotelDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(hotel.getId(), responseDto.getId());
    assertEquals(hotel.getAddress().getId(), responseDto.getAddressId());
    assertEquals(hotel.getName(), responseDto.getName());
    assertEquals(hotel.getDescription(), responseDto.getDescription());
  }

  @Test
  void findAll() {
    // when
    when(hotelService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<HotelDto>> responseEntity = hotelController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
