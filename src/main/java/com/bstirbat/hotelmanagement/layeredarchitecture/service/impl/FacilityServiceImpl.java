package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.FacilityMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.FacilityRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.FacilityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
}
