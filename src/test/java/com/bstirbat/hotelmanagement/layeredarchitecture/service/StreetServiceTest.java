package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.StreetRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.StreetServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class StreetServiceTest {

  private StreetService streetService;

  private CityService cityService;
  private StreetRepository streetRepository;

  private Street street;
  private StreetCreateDto dto;

  @BeforeEach
  public void setUp() {
    streetRepository = mock(StreetRepository.class);
    cityService = mock(CityService.class);

    streetService = new StreetServiceImpl(cityService, streetRepository);

    Country country = CountryGenerator.CountryBuilder.builder()
        .withId(1L)
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    City city = CityGenerator.CityBuilder.builder()
        .withId(2L)
        .withName("Berlin")
        .withCountry(country)
        .build();

    street = StreetGenerator.StreetBuilder.builder()
        .withId(3L)
        .withName("Street")
        .withCity(city)
        .build();

    dto = StreetGenerator.StreetCreateDtoBuilder.builder()
        .withCityId(city.getId())
        .withName("Street")
        .build();
  }

  @Test
  void create() {
    // given
    when(streetRepository.save(any())).thenReturn(street);

    // when
    Street createdStreet = streetService.create(dto);

    // then
    assertEquals(street.getId(), createdStreet.getId());
    assertEquals(street.getCity().getId(), createdStreet.getCity().getId());
    assertEquals(street.getName(), createdStreet.getName());
  }

  @Test
  void create_whenInvalidCityId() {
    // given
    when(cityService.getById(2L)).thenThrow(new ResourceNotFoundException("Could not find a city with id 1"));

    // when & then
    assertThrows(InvalidDataException.class, () -> streetService.create(dto));
  }

  @Test
  void getById() {
    // given
    when(streetRepository.findById(3L)).thenReturn(Optional.of(street));

    // when
    Street retrievedStreet = streetService.getById(3L);

    // then
    assertEquals(street.getId(), retrievedStreet.getId());
    assertEquals(street.getCity().getId(), retrievedStreet.getCity().getId());
    assertEquals(street.getName(), retrievedStreet.getName());
  }

  @Test
  void getById_whenNoStreetFound() {
    // given
    when(streetRepository.findById(1L)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ResourceNotFoundException.class, () -> streetService.getById(1L));
  }

  @Test
  void findAll() {
    // given
    Page<Street> page = Page.empty();

    when(streetRepository.findAll(any(Pageable.class))).thenReturn(page);

    // when
    Page<Street> streets = streetService.findAll(Pageable.unpaged());

    // then
    assertTrue(streets.isEmpty());
  }
}
