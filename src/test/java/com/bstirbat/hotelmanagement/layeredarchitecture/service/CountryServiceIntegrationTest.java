package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
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
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    Country country = countryService.create(createDto);

    assertNotNull(country.getId());
    assertEquals(createDto.getName(), country.getName());
    assertEquals(createDto.getCountryCode(), country.getCountryCode());
  }

  @Test
  void createCountry_whenInvalidDto() {
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> countryService.create(new CountryCreateDto()));

    assertTrue(ex.getMessage().contains("name"));
    assertTrue(ex.getMessage().contains("countryCode"));
  }

  @Test
  void createCountry_whenInvalidCountryCode() {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE1");

    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> countryService.create(createDto));

    assertTrue(ex.getMessage().contains("countryCode"));
  }

  @Test
  void createCountry_whenNullDto() {
    assertThrows(ConstraintViolationException.class,
        () -> countryService.create(null));
  }

  @Test
  void getByd() {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    Country country = countryService.create(createDto);

    Country retrievedCountry = countryService.getById(country.getId());

    assertEquals(country.getId(), retrievedCountry.getId());
    assertEquals(country.getName(), retrievedCountry.getName());
    assertEquals(country.getCountryCode(), retrievedCountry.getCountryCode());
  }

  @Test
  void getByd_whenNoCountryFound() {
    assertThrows(ResourceNotFoundException.class, () -> countryService.getById(1L));
  }

  @Test
  void getByd_whenNullId() {
    assertThrows(ConstraintViolationException.class, () -> countryService.getById(null));
  }

  @Test
  void findAll() {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    countryService.create(createDto);

    Page<Country> countries = countryService.findAll(Pageable.unpaged());

    assertEquals(1,  countries.getTotalElements());

    Country foundCountry = countries.getContent().get(0);
    assertNotNull(foundCountry.getId());
    assertEquals(createDto.getName(), foundCountry.getName());
    assertEquals(createDto.getCountryCode(), foundCountry.getCountryCode());
  }

  @Test
  void findAll_whenNullArgument() {
    assertThrows(ConstraintViolationException.class,
        () -> countryService.findAll(null));
  }
}
