package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.AddressGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator.StreetCreateDtoBuilder;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class AddressServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private AddressService addressService;

  @Autowired
  private StreetService streetService;

  @Autowired
  private CityService cityService;

  @Autowired
  private CountryService countryService;

  private Street street;

  @BeforeEach
  public void setUp() {
    CountryCreateDto countryCreateDto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    Country country = countryService.create(countryCreateDto);

    CityCreateDto cityCreateDto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(country.getId())
        .withName("Berlin")
        .build();

    City city = cityService.create(cityCreateDto);

    StreetCreateDto createDto = StreetCreateDtoBuilder.builder()
        .withCityId(city.getId())
        .withName("Street")
        .build();

    street = streetService.create(createDto);
  }

  @Test
  void create() {
    // given
    AddressCreateDto createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    // when
    Address address = addressService.create(createDto);

    // then
    assertNotNull(address.getId());
    assertEquals(createDto.getStreetId(), address.getStreet().getId());
    assertEquals(createDto.getHouseNumber(), address.getHouseNumber());
    assertEquals(createDto.getPostalCode(), address.getPostalCode());
  }

  @Test
  void create_whenInvalidStreetId() {
    // given
    AddressCreateDto createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(-1L)
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    // when & then
    assertThrows(InvalidDataException.class,
        () -> addressService.create(createDto));
  }

  @Test
  void create_whenInvalidDto() {
    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> addressService.create(new AddressCreateDto()));

    // then
    assertTrue(ex.getMessage().contains("streetId"));
    assertTrue(ex.getMessage().contains("houseNumber"));
    assertTrue(ex.getMessage().contains("postalCode"));
  }

  @Test
  void create_whenNullDto() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> addressService.create(null));
  }

  @Test
  void getByd() {
    // given
    AddressCreateDto createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    Address address = addressService.create(createDto);

    // when
    Address retrievedAddress = addressService.getById(address.getId());

    // then
    assertEquals(address.getId(), retrievedAddress.getId());
    assertEquals(address.getStreet().getId(), retrievedAddress.getStreet().getId());
    assertEquals(address.getHouseNumber(), retrievedAddress.getHouseNumber());
    assertEquals(address.getPostalCode(), retrievedAddress.getPostalCode());
  }

  @Test
  void getByd_whenNoAddressFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> addressService.getById(1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> addressService.getById(null));
  }

  @Test
  void findAll() {
    // given
    AddressCreateDto createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    Address address = addressService.create(createDto);

    // when
    Page<Address> addresses = addressService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  addresses.getTotalElements());

    Address foundAddress = addresses.getContent().get(0);
    assertEquals(address.getId(), foundAddress.getId());
    assertEquals(address.getStreet().getId(), foundAddress.getStreet().getId());
    assertEquals(address.getHouseNumber(), foundAddress.getHouseNumber());
    assertEquals(address.getPostalCode(), foundAddress.getPostalCode());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> addressService.findAll(null));
  }
}
