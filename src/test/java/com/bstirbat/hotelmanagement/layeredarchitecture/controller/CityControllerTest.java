package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CityDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

class CityControllerTest {

  private CityService cityService;
  private CityController cityController;

  private Country country;

  @BeforeEach
  public void setUp() {
    cityService = mock(CityService.class);
    cityController = new CityController(cityService);

    country = CountryGenerator.CountryBuilder.builder()
        .withId(1L)
        .withName("Germany")
        .withCountryCode("DE")
        .build();
  }

  @Test
  void create() {
    // given
    CityCreateDto createDto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(1L)
        .withName("Berlin")
        .build();

    City city = CityGenerator.CityBuilder.builder()
        .withId(2L)
        .withCountry(country)
        .withName("Berlin")
        .build();

    when(cityService.create(any())).thenReturn(city);

    // when
    ResponseEntity<CityDto> responseEntity = cityController.create(createDto);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    CityDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(city.getId(), responseDto.getId());
    assertEquals(city.getCountry().getId(), responseDto.getCountryId());
    assertEquals(city.getName(), responseDto.getName());
  }

  @Test
  void getById() {
    // given
    City city = CityGenerator.CityBuilder.builder()
        .withId(2L)
        .withCountry(country)
        .withName("Berlin")
        .build();

    when(cityService.getById(2L)).thenReturn(city);

    // when
    ResponseEntity<CityDto> responseEntity = cityController.getById(2L);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    CityDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(city.getId(), responseDto.getId());
    assertEquals(city.getCountry().getId(), responseDto.getCountryId());
    assertEquals(city.getName(), responseDto.getName());
  }

  @Test
  void findAll() {
    // when
    when(cityService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<CityDto>> responseEntity = cityController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
