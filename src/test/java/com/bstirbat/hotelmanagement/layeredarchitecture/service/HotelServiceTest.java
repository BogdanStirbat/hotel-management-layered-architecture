package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.HotelRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.HotelServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.AddressGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class HotelServiceTest {

  private HotelService hotelService;

  private AddressService addressService;
  private HotelRepository hotelRepository;

  private Address address;

  @BeforeEach
  public void setUp() {
    addressService = mock(AddressService.class);
    hotelRepository = mock(HotelRepository.class);

    hotelService = new HotelServiceImpl(addressService, hotelRepository);

    address = AddressGenerator.AddressBuilder.builder()
        .withId(4L)
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();
  }

  @Test
  void create() {
    // given
    HotelCreateDto dto = HotelGenerator.HotelCreateDtoBuilder.builder()
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

    when(hotelRepository.save(any())).thenReturn(hotel);

    // when
    Hotel createdHotel = hotelService.create(dto);

    // then
    assertEquals(hotel.getId(), createdHotel.getId());
    assertEquals(hotel.getAddress().getId(), createdHotel.getAddress().getId());
    assertEquals(hotel.getName(), createdHotel.getName());
    assertEquals(hotel.getDescription(), createdHotel.getDescription());
  }

  @Test
  void create_whenInvalidAddressId() {
    // given
    HotelCreateDto dto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    when(addressService.getById(address.getId())).thenThrow(new ResourceNotFoundException("Could not find an address with id 4"));

    // when & then
    assertThrows(InvalidDataException.class, () -> hotelService.create(dto));
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

    when(hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel));

    // when
    Hotel retrievedHotel = hotelService.getById(hotel.getId());

    // then
    assertEquals(hotel.getId(), retrievedHotel.getId());
    assertEquals(hotel.getAddress().getId(), retrievedHotel.getAddress().getId());
    assertEquals(hotel.getName(), retrievedHotel.getName());
    assertEquals(hotel.getDescription(), retrievedHotel.getDescription());
  }

  @Test
  void getById_whenNoHotelFound() {
    // given
    when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ResourceNotFoundException.class, () -> hotelService.getById(1L));
  }

  @Test
  void findAll() {
    // given
    Page<Hotel> page = Page.empty();

    when(hotelRepository.findAll(any(Pageable.class))).thenReturn(page);

    // when
    Page<Hotel> hotels = hotelService.findAll(Pageable.unpaged());

    // then
    assertTrue(hotels.isEmpty());
  }
}
