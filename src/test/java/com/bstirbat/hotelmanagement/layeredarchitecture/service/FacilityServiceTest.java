package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.FacilityRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.FacilityServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.FacilityGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class FacilityServiceTest {

  private FacilityService facilityService;

  private FacilityRepository facilityRepository;


  @BeforeEach
  public void setUp() {
    facilityRepository = mock(FacilityRepository.class);
    facilityService = new FacilityServiceImpl(facilityRepository);
  }

  @Test
  void create() {
    // given
    FacilityCreateDto dto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    Facility facility = FacilityGenerator.FacilityBuilder.builder()
        .withId(1L)
        .withName("Free WiFi")
        .build();

    when(facilityRepository.save(any())).thenReturn(facility);

    // when
    Facility createdFacility = facilityService.create(dto);

    // then
    assertEquals(facility.getId(), createdFacility.getId());
    assertEquals(facility.getName(), createdFacility.getName());
  }

  @Test
  void getById() {
    // given
    Facility facility = FacilityGenerator.FacilityBuilder.builder()
        .withId(1L)
        .withName("Free WiFi")
        .build();

    when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));

    // when
    Facility retrievedFacility = facilityService.getById(facility.getId());

    // then
    assertEquals(facility.getId(), retrievedFacility.getId());
    assertEquals(facility.getName(), retrievedFacility.getName());
  }

  @Test
  void getById_whenNoFacilityFound() {
    // given
    when(facilityRepository.findById(1L)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ResourceNotFoundException.class, () -> facilityService.getById(1L));
  }

  @Test
  void findAll() {
    // given
    Page<Facility> page = Page.empty();

    when(facilityRepository.findAll(any(Pageable.class))).thenReturn(page);

    // when
    Page<Facility> facilities = facilityService.findAll(Pageable.unpaged());

    // then
    assertTrue(facilities.isEmpty());
  }
}
