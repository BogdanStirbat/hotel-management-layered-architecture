package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.helper.AddressHelper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class HotelServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private CountryService countryService;

  @Autowired
  private CityService cityService;

  @Autowired
  private StreetService streetService;

  @Autowired
  private AddressService addressService;

  @Autowired
  private HotelService hotelService;

  private Address address;

  @BeforeEach
  public void setUp() {
    AddressHelper addressHelper = new AddressHelper(countryService, cityService, streetService, addressService);
    address = addressHelper.createAnAddress();
  }

  @Test
  void create() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    // when
    Hotel hotel = hotelService.create(createDto);

    // then
    assertNotNull(hotel.getId());
    assertEquals(createDto.getAddressId(), hotel.getAddress().getId());
    assertEquals(createDto.getName(), hotel.getName());
    assertEquals(createDto.getDescription(), hotel.getDescription());
  }

  @Test
  void create_whenInvalidAddressId() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(-1L)
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    // when & then
    assertThrows(InvalidDataException.class,
        () -> hotelService.create(createDto));
  }

  @Test
  void create_whenInvalidDto() {
    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> hotelService.create(new HotelCreateDto()));

    // then
    assertTrue(ex.getMessage().contains("addressId"));
    assertTrue(ex.getMessage().contains("name"));
    assertTrue(ex.getMessage().contains("description"));
  }

  @Test
  void create_whenNullDto() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> hotelService.create(null));
  }

  @Test
  void getByd() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    Hotel hotel = hotelService.create(createDto);

    // when
    Hotel retrievedHotel = hotelService.getById(hotel.getId());

    // then
    assertEquals(hotel.getId(), retrievedHotel.getId());
    assertEquals(hotel.getAddress().getId(), retrievedHotel.getAddress().getId());
    assertEquals(hotel.getName(), retrievedHotel.getName());
    assertEquals(hotel.getDescription(), retrievedHotel.getDescription());
  }

  @Test
  void getByd_whenNoHotelFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> hotelService.getById(-1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> hotelService.getById(null));
  }

  @Test
  void findAll() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    Hotel hotel = hotelService.create(createDto);

    // when
    Page<Hotel> hotels = hotelService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  hotels.getTotalElements());

    Hotel foundHotel = hotels.getContent().get(0);
    assertEquals(hotel.getId(), foundHotel.getId());
    assertEquals(hotel.getAddress().getId(), foundHotel.getAddress().getId());
    assertEquals(hotel.getName(), foundHotel.getName());
    assertEquals(hotel.getDescription(), foundHotel.getDescription());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> hotelService.findAll(null));
  }
}
