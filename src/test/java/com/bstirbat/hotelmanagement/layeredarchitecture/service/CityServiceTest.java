package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.CityRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.CityServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class CityServiceTest {

  private CityService cityService;

  private CityRepository cityRepository;
  private CountryService countryService;

  @BeforeEach
  public void setUp() {
    cityRepository = mock(CityRepository.class);
    countryService = mock(CountryService.class);

    cityService = new CityServiceImpl(cityRepository, countryService);
  }

  @Test
  void create() {
    // given
    Country country = CountryGenerator.CountryBuilder.builder()
        .withId(1L)
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    CityCreateDto dto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(1L)
        .withName("Berlin")
        .build();

    City city = CityGenerator.CityBuilder.builder()
        .withId(2L)
        .withCountry(country)
        .withName("Berlin")
        .build();

    when(cityRepository.save(any())).thenReturn(city);

    // when
    City createdCity = cityService.create(dto);

    // then
    assertEquals(city.getId(), createdCity.getId());
    assertEquals(city.getCountry().getId(), createdCity.getCountry().getId());
    assertEquals(city.getName(), createdCity.getName());
  }

  @Test
  void create_whenInvalidCountryId() {
    // given
    CityCreateDto dto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(1L)
        .withName("Berlin")
        .build();

    when(countryService.getById(any())).thenThrow(new ResourceNotFoundException("Could not find a country with id 1"));

    // when & then
    assertThrows(InvalidDataException.class, () -> cityService.create(dto));
  }

  @Test
  void getById() {
    // given
    Country country = CountryGenerator.CountryBuilder.builder()
        .withId(1L)
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    City city = CityGenerator.CityBuilder.builder()
        .withId(2L)
        .withCountry(country)
        .withName("Berlin")
        .build();

    when(cityRepository.findById(2L)).thenReturn(Optional.of(city));

    // when
    City retrievedCity = cityService.getById(2L);

    // then
    assertEquals(city.getId(), retrievedCity.getId());
    assertEquals(city.getCountry().getId(), retrievedCity.getCountry().getId());
    assertEquals(city.getName(), retrievedCity.getName());
  }

  @Test
  void getById_whenNoCountryFound() {
    // given
    when(cityRepository.findById(1L)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ResourceNotFoundException.class, () -> cityService.getById(1L));
  }

  @Test
  void findAll() {
    // given
    Page<City> page = Page.empty();

    when(cityRepository.findAll(any(Pageable.class))).thenReturn(page);

    // when
    Page<City> cities = cityService.findAll(Pageable.unpaged());

    // then
    assertTrue(cities.isEmpty());
  }
}
