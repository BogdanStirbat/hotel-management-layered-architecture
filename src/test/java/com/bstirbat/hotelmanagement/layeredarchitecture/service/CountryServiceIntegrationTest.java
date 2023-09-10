package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class CountryServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private CountryService countryService;

  @Test
  void createCountry() {
    // given
    CountryCreateDto createDto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    // when
    Country country = countryService.create(createDto);

    // then
    assertNotNull(country.getId());
    assertEquals(createDto.getName(), country.getName());
    assertEquals(createDto.getCountryCode(), country.getCountryCode());
  }

  @Test
  void createCountry_whenInvalidDto() {
    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> countryService.create(new CountryCreateDto()));

    // then
    assertTrue(ex.getMessage().contains("name"));
    assertTrue(ex.getMessage().contains("countryCode"));
  }

  @Test
  void createCountry_whenInvalidCountryCode() {
    // given
    CountryCreateDto createDto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE1")
        .build();

    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> countryService.create(createDto));

    // then
    assertTrue(ex.getMessage().contains("countryCode"));
  }

  @Test
  void createCountry_whenNullDto() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> countryService.create(null));
  }

  @Test
  void getByd() {
    // given
    CountryCreateDto createDto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    Country country = countryService.create(createDto);

    // when
    Country retrievedCountry = countryService.getById(country.getId());

    // then
    assertEquals(country.getId(), retrievedCountry.getId());
    assertEquals(country.getName(), retrievedCountry.getName());
    assertEquals(country.getCountryCode(), retrievedCountry.getCountryCode());
  }

  @Test
  void getByd_whenNoCountryFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> countryService.getById(1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> countryService.getById(null));
  }

  @Test
  void findAll() {
    // given
    CountryCreateDto createDto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    countryService.create(createDto);

    // when
    Page<Country> countries = countryService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  countries.getTotalElements());

    Country foundCountry = countries.getContent().get(0);
    assertNotNull(foundCountry.getId());
    assertEquals(createDto.getName(), foundCountry.getName());
    assertEquals(createDto.getCountryCode(), foundCountry.getCountryCode());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> countryService.findAll(null));
  }
}
