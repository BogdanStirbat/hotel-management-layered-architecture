package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CountryDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;


class CountryControllerTest {

  private CountryService countryService;
  private CountryController countryController;

  @BeforeEach
  public void setUp() {
    countryService = mock(CountryService.class);
    countryController = new CountryController(countryService);
  }

  @Test
  void createCountry() {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    Country country = new Country();
    country.setId(1L);
    country.setName("Germany");
    country.setCountryCode("DE");

    when(countryService.create(any())).thenReturn(country);

    ResponseEntity<CountryDto> responseEntity = countryController.create(createDto);
    assertEquals(CREATED, responseEntity.getStatusCode());

    CountryDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(country.getId(), responseDto.getId());
    assertEquals(country.getName(), responseDto.getName());
    assertEquals(country.getCountryCode(), responseDto.getCountryCode());
  }

  @Test
  void getById() {
    Country country = new Country();
    country.setId(1L);
    country.setName("Germany");
    country.setCountryCode("DE");

    when(countryService.getById(1L)).thenReturn(country);

    ResponseEntity<CountryDto> responseEntity = countryController.getById(1L);
    assertEquals(OK, responseEntity.getStatusCode());

    CountryDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(country.getId(), responseDto.getId());
    assertEquals(country.getName(), responseDto.getName());
    assertEquals(country.getCountryCode(), responseDto.getCountryCode());
  }

  @Test
  void findAll() {
    when(countryService.findAll(any())).thenReturn(Page.empty());

    ResponseEntity<Page<CountryDto>> responseEntity = countryController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
