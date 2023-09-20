package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.FacilityGenerator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class FacilityServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private FacilityService facilityService;

  @Test
  void create() {
    // given
    FacilityCreateDto createDto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    // when
    Facility facility = facilityService.create(createDto);

    // then
    assertNotNull(facility.getId());
    assertEquals(createDto.getName(), facility.getName());
  }

  @Test
  void create_whenInvalidDto() {
    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> facilityService.create(new FacilityCreateDto()));

    // then
    assertTrue(ex.getMessage().contains("name"));
  }

  @Test
  void create_whenNullDto() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> facilityService.create(null));
  }

  @Test
  void getByd() {
    // given
    FacilityCreateDto createDto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    Facility facility = facilityService.create(createDto);

    // when
    Facility retrievedFacility = facilityService.getById(facility.getId());

    // then
    assertEquals(facility.getId(), retrievedFacility.getId());
    assertEquals(facility.getName(), retrievedFacility.getName());
  }

  @Test
  void getByd_whenNoRoomTypeFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> facilityService.getById(-1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> facilityService.getById(null));
  }

  @Test
  void findAll() {
    // given
    FacilityCreateDto createDto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    Facility facility = facilityService.create(createDto);

    // when
    Page<Facility> facilities = facilityService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  facilities.getTotalElements());

    Facility foundFacility = facilities.getContent().get(0);
    assertEquals(facility.getId(), foundFacility.getId());
    assertEquals(facility.getName(), foundFacility.getName());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> facilityService.findAll(null));
  }
}
