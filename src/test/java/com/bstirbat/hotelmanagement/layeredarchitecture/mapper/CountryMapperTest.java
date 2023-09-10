package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CountryDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import org.junit.jupiter.api.Test;

class CountryMapperTest {

  @Test
  void toEntity() {
    // given
    CountryCreateDto dto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    // when
    Country entity = CountryMapper.INSTANCE.toEntity(dto);

    // then
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getCountryCode(), entity.getCountryCode());
  }

  @Test
  void toDto() {
    // given
    Country entity = CountryGenerator.CountryBuilder.builder()
        .withId(1L)
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    // when
    CountryDto dto = CountryMapper.INSTANCE.toDto(entity);

    // then
    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getCountryCode(), entity.getCountryCode());
  }
}
