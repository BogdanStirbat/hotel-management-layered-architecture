package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.CountryMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.CountryRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class CountryServiceImpl implements CountryService {

  private final CountryRepository countryRepository;

  @Autowired
  public CountryServiceImpl(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  @Override
  public Page<Country> findAll(@NotNull Pageable pageable) {

    return countryRepository.findAll(pageable);
  }

  @Override
  public Country getById(@NotNull Long id) {

    return countryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(String.format("Could not find country with id %s", id)));
  }

  @Override
  public Country create(@NotNull @Valid CountryCreateDto createDto) {
    Country country = CountryMapper.INSTANCE.toEntity(createDto);

    return countryRepository.save(country);
  }
}
