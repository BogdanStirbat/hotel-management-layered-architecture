package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.FacilityMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.FacilityRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.FacilityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class FacilityServiceImpl implements FacilityService {

  private final FacilityRepository facilityRepository;

  @Autowired
  public FacilityServiceImpl(FacilityRepository facilityRepository) {
    this.facilityRepository = facilityRepository;
  }

  @Override
  public Facility create(@NotNull @Valid FacilityCreateDto createDto) {
    Facility imageReference = FacilityMapper.INSTANCE.toEntity(createDto);

    return facilityRepository.save(imageReference);
  }

  @Override
  public Facility getById(@NotNull Long id) {

    return facilityRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find facility with id %s", id)));
  }

  @Override
  public Page<Facility> findAll(@NotNull Pageable pageable) {

    return facilityRepository.findAll(pageable);
  }
}
