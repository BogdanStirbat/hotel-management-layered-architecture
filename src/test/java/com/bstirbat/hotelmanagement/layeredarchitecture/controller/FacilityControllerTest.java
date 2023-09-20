package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.FacilityDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.FacilityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.FacilityGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

class FacilityControllerTest {

  private FacilityService facilityService;

  private FacilityController facilityController;

  @BeforeEach
  public void setUp() {
    facilityService = mock(FacilityService.class);
    facilityController = new FacilityController(facilityService);
  }

  @Test
  void create() {
    // given
    FacilityCreateDto createDto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    Facility facility = FacilityGenerator.FacilityBuilder.builder()
        .withId(1L)
        .withName("Free WiFi")
        .build();

    when(facilityService.create(any())).thenReturn(facility);

    // when
    ResponseEntity<FacilityDto> responseEntity = facilityController.create(createDto);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    FacilityDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(facility.getId(), responseDto.getId());
    assertEquals(facility.getName(), responseDto.getName());
  }

  @Test
  void getById() {
    // given
    Facility facility = FacilityGenerator.FacilityBuilder.builder()
        .withId(1L)
        .withName("Free WiFi")
        .build();

    when(facilityService.getById(facility.getId())).thenReturn(facility);

    // when
    ResponseEntity<FacilityDto> responseEntity = facilityController.getById(facility.getId());

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    FacilityDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(facility.getId(), responseDto.getId());
    assertEquals(facility.getName(), responseDto.getName());
  }

  @Test
  void findAll() {
    // when
    when(facilityService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<FacilityDto>> responseEntity = facilityController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
