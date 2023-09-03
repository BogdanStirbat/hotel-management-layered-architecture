package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CountryDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import org.junit.jupiter.api.Test;

class CountryMapperTest {

  @Test
  void toEntity() {
    CountryCreateDto dto = new CountryCreateDto();
    dto.setName("Germany");
    dto.setCountryCode("DE");

    Country entity = CountryMapper.INSTANCE.toEntity(dto);

    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getCountryCode(), entity.getCountryCode());
  }

  @Test
  void toDto() {
    Country entity = new Country();
    entity.setId(1L);
    entity.setName("Germany");
    entity.setCountryCode("DE");

    CountryDto dto = CountryMapper.INSTANCE.toDto(entity);

    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getCountryCode(), entity.getCountryCode());
  }
}
