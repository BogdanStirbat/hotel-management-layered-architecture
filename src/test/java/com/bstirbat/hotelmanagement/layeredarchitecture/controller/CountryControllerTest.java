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
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
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
    // given
    CountryCreateDto createDto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    Country country = CountryGenerator.CountryBuilder.builder()
        .withId(1L)
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    when(countryService.create(any())).thenReturn(country);

    // when
    ResponseEntity<CountryDto> responseEntity = countryController.create(createDto);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    CountryDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(country.getId(), responseDto.getId());
    assertEquals(country.getName(), responseDto.getName());
    assertEquals(country.getCountryCode(), responseDto.getCountryCode());
  }

  @Test
  void getById() {
    // given
    Country country = CountryGenerator.CountryBuilder.builder()
        .withId(1L)
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    when(countryService.getById(1L)).thenReturn(country);

    // when
    ResponseEntity<CountryDto> responseEntity = countryController.getById(1L);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    CountryDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(country.getId(), responseDto.getId());
    assertEquals(country.getName(), responseDto.getName());
    assertEquals(country.getCountryCode(), responseDto.getCountryCode());
  }

  @Test
  void findAll() {
    // when
    when(countryService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<CountryDto>> responseEntity = countryController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
