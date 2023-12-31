package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.ExceptionWrapperUtils.wrapWithInvalidDataException;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.StreetMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.StreetRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class StreetServiceImpl implements StreetService {

  private final CityService cityService;
  private final StreetRepository streetRepository;

  @Autowired
  public StreetServiceImpl(CityService cityService, StreetRepository streetRepository) {
    this.cityService = cityService;
    this.streetRepository = streetRepository;
  }

  @Override
  public Street create(@NotNull @Valid StreetCreateDto createDto) {
    Street street = StreetMapper.INSTANCE.toEntity(createDto);
    street.setCity(wrapWithInvalidDataException(() -> cityService.getById(createDto.getCityId())));

    return streetRepository.save(street);
  }

  @Override
  public Street getById(@NotNull Long id) {

    return streetRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find street with id %s", id)));
  }

  @Override
  public Page<Street> findAll(@NotNull Pageable pageable) {

    return streetRepository.findAll(pageable);
  }
}
