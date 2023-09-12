package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator.StreetCreateDtoBuilder;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class StreetServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private StreetService streetService;

  @Autowired
  private CityService cityService;

  @Autowired
  private CountryService countryService;

  private City city;

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

    city = cityService.create(cityCreateDto);
  }

  @Test
  void create() {
    // given
    StreetCreateDto createDto = StreetCreateDtoBuilder.builder()
        .withCityId(city.getId())
        .withName("Street")
        .build();

    // when
    Street street = streetService.create(createDto);

    // then
    assertNotNull(street.getId());
    assertEquals(createDto.getCityId(), street.getCity().getId());
    assertEquals(createDto.getName(), street.getName());
  }

  @Test
  void create_whenInvalidCountryId() {
    // given
    StreetCreateDto createDto = StreetCreateDtoBuilder.builder()
        .withCityId(-1L)
        .withName("Street")
        .build();

    // when & then
    assertThrows(InvalidDataException.class,
        () -> streetService.create(createDto));
  }

  @Test
  void create_whenInvalidDto() {
    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> streetService.create(new StreetCreateDto()));

    // then
    assertTrue(ex.getMessage().contains("cityId"));
    assertTrue(ex.getMessage().contains("name"));
  }

  @Test
  void create_whenNullDto() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> streetService.create(null));
  }

  @Test
  void getByd() {
    // given
    StreetCreateDto createDto = StreetCreateDtoBuilder.builder()
        .withCityId(city.getId())
        .withName("Street")
        .build();

    Street street = streetService.create(createDto);

    // when
    Street retrievedStreet = streetService.getById(street.getId());

    // then
    assertEquals(street.getId(), retrievedStreet.getId());
    assertEquals(street.getCity().getId(), retrievedStreet.getCity().getId());
    assertEquals(street.getName(), retrievedStreet.getName());
  }

  @Test
  void getByd_whenNoStreetFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> streetService.getById(1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> streetService.getById(null));
  }

  @Test
  void findAll() {
    // given
    StreetCreateDto createDto = StreetCreateDtoBuilder.builder()
        .withCityId(city.getId())
        .withName("Street")
        .build();

    Street street = streetService.create(createDto);

    // when
    Page<Street> streets = streetService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  streets.getTotalElements());

    Street foundStreet = streets.getContent().get(0);
    assertEquals(street.getId(), foundStreet.getId());
    assertEquals(street.getCity().getId(), foundStreet.getCity().getId());
    assertEquals(street.getName(), foundStreet.getName());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> streetService.findAll(null));
  }
}
