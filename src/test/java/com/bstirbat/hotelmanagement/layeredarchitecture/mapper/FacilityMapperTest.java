package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.FacilityDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.FacilityGenerator;
import org.junit.jupiter.api.Test;

class FacilityMapperTest {

  @Test
  void toEntity() {
    // given
    FacilityCreateDto dto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    // when
    Facility entity = FacilityMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertEquals(dto.getName(), entity.getName());
  }

  @Test
  void toDto() {
    // given
    Facility facility = FacilityGenerator.FacilityBuilder.builder()
        .withId(1L)
        .withName("Free WiFi")
        .build();

    // when
    FacilityDto dto = FacilityMapper.INSTANCE.toDto(facility);

    // then
    assertEquals(dto.getId(), facility.getId());
    assertEquals(dto.getName(), facility.getName());
  }
}
