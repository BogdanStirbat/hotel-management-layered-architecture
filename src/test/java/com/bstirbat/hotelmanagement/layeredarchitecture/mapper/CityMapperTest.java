package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CityDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import org.junit.jupiter.api.Test;

class CityMapperTest {

  @Test
  void toEntity() {
    // given
    CityCreateDto dto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(1L)
        .withName("Berlin")
        .build();

    // when
    City entity = CityMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertNull(entity.getCountry());
    assertEquals(dto.getName(), entity.getName());
  }

  @Test
  void toDto() {
    // given
    Country country = CountryGenerator.CountryBuilder.builder()
        .withId(1L)
        .withName("Germany")
        .withCountryCode("DE")
        .build();

    City city = CityGenerator.CityBuilder.builder()
        .withId(2L)
        .withCountry(country)
        .withName("Berlin")
        .build();

    // when
    CityDto dto = CityMapper.INSTANCE.toDto(city);

    // then
    assertEquals(dto.getId(), city.getId());
    assertEquals(dto.getCountryId(), city.getCountry().getId());
    assertEquals(dto.getName(), city.getName());
  }
}
