package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.CountryRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.CountryServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class CountryServiceTest {

  private CountryService countryService;
  private CountryRepository countryRepository;

  @BeforeEach
  public void setUp() {
    countryRepository = mock(CountryRepository.class);
    countryService = new CountryServiceImpl(countryRepository);
  }

  @Test
  void create() {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    Country country = new Country();
    country.setId(1L);
    country.setName("Germany");
    country.setCountryCode("DE");

    when(countryRepository.save(any())).thenReturn(country);

    Country createdCountry = countryService.create(createDto);

    assertEquals(country.getId(), createdCountry.getId());
    assertEquals(country.getName(), createdCountry.getName());
    assertEquals(country.getCountryCode(), createdCountry.getCountryCode());
  }

  @Test
  void getById() {
    Country country = new Country();
    country.setId(1L);
    country.setName("Germany");
    country.setCountryCode("DE");

    when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

    Country retrievedCountry = countryService.getById(1L);

    assertEquals(country.getId(), retrievedCountry.getId());
    assertEquals(country.getName(), retrievedCountry.getName());
    assertEquals(country.getCountryCode(), retrievedCountry.getCountryCode());
  }

  @Test
  void getById_whenNoCountryFound() {
    when(countryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> countryService.getById(1L));
  }

  @Test
  void findAll() {
    Page<Country> page = Page.empty();

    when(countryRepository.findAll(any(Pageable.class))).thenReturn(page);

    Page<Country> countries = countryService.findAll(Pageable.unpaged());

    assertTrue(countries.isEmpty());
  }
}
