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
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class CityServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private CountryService countryService;

  @Autowired
  private CityService cityService;

  private Country country;

  @BeforeEach
  public void setUp() {
    CountryCreateDto createDto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    country = countryService.create(createDto);
  }

  @Test
  void create() {
    // given
    CityCreateDto createDto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(country.getId())
        .withName("Berlin")
        .build();

    // when
    City city = cityService.create(createDto);

    // then
    assertNotNull(city.getId());
    assertEquals(createDto.getCountryId(), city.getCountry().getId());
    assertEquals(createDto.getName(), city.getName());
  }

  @Test
  void create_whenInvalidCountryId() {
    // given
    CityCreateDto createDto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(-1L)
        .withName("Berlin")
        .build();

    // when & then
    assertThrows(InvalidDataException.class,
        () -> cityService.create(createDto));
  }

  @Test
  void create_whenInvalidDto() {
    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> cityService.create(new CityCreateDto()));

    // then
    assertTrue(ex.getMessage().contains("countryId"));
    assertTrue(ex.getMessage().contains("name"));
  }

  @Test
  void create_whenNullDto() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> cityService.create(null));
  }

  @Test
  void getByd() {
    // given
    CityCreateDto createDto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(country.getId())
        .withName("Berlin")
        .build();

    City city = cityService.create(createDto);

    // when
    City retrievedCity = cityService.getById(city.getId());

    // then
    assertEquals(city.getId(), retrievedCity.getId());
    assertEquals(city.getCountry().getId(), retrievedCity.getCountry().getId());
    assertEquals(city.getName(), retrievedCity.getName());
  }

  @Test
  void getByd_whenNoCityFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> cityService.getById(1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> cityService.getById(null));
  }

  @Test
  void findAll() {
    // given
    CityCreateDto createDto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(country.getId())
        .withName("Berlin")
        .build();

    City city = cityService.create(createDto);

    // when
    Page<City> cities = cityService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  cities.getTotalElements());

    City foundCity = cities.getContent().get(0);
    assertEquals(city.getId(), foundCity.getId());
    assertEquals(city.getCountry().getId(), foundCity.getCountry().getId());
    assertEquals(city.getName(), foundCity.getName());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> cityService.findAll(null));
  }
}
