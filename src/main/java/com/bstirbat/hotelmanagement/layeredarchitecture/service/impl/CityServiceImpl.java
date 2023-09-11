package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.ExceptionWrapperUtils.wrapWithInvalidDataException;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.CityMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.CityRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CityServiceImpl implements CityService {

  private final CityRepository cityRepository;
  private final CountryService countryService;

  @Autowired
  public CityServiceImpl(CityRepository cityRepository, CountryService countryService) {
    this.cityRepository = cityRepository;
    this.countryService = countryService;
  }

  @Override
  public City create(@NotNull @Valid CityCreateDto createDto) {

    City city = CityMapper.INSTANCE.toEntity(createDto);
    city.setCountry(wrapWithInvalidDataException(() -> countryService.getById(createDto.getCountryId())));

    return cityRepository.save(city);
  }

  @Override
  public City getById(@NotNull Long id) {

    return cityRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find city with id %s", id)));
  }

  @Override
  public Page<City> findAll(@NotNull Pageable pageable) {

    return cityRepository.findAll(pageable);
  }
}
