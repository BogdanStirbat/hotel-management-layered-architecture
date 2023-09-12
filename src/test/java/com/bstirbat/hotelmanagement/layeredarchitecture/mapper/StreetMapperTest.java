package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.StreetDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator;
import org.junit.jupiter.api.Test;

class StreetMapperTest {

  @Test
  void toEntity() {
    // given
    StreetCreateDto dto = StreetGenerator.StreetCreateDtoBuilder.builder()
        .withCityId(1L)
        .withName("Street")
        .build();

    // when
    Street entity = StreetMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertNull(entity.getCity());
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
        .withName("Berlin")
        .withCountry(country)
        .build();

    Street street = StreetGenerator.StreetBuilder.builder()
        .withId(2L)
        .withName("Street")
        .withCity(city)
        .build();

    // when
    StreetDto dto = StreetMapper.INSTANCE.toDto(street);

    // then
    assertEquals(dto.getId(), street.getId());
    assertEquals(dto.getCityId(), street.getCity().getId());
    assertEquals(dto.getName(), street.getName());
  }
}
