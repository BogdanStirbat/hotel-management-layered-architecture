package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.AddressGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator;
import org.junit.jupiter.api.Test;

class AddressMapperTest {

  @Test
  void toEntity() {
    // given
    AddressCreateDto dto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(1L)
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    // when
    Address entity = AddressMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertNull(entity.getStreet());
    assertEquals(dto.getHouseNumber(), entity.getHouseNumber());
    assertEquals(dto.getPostalCode(), entity.getPostalCode());
  }

  @Test
  void toDto() {
    // given
    Street street = StreetGenerator.StreetBuilder.builder()
        .withId(1L)
        .withName("Street")
        .build();

    Address address = AddressGenerator.AddressBuilder.builder()
        .withId(2L)
        .withStreet(street)
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    // when
    AddressDto dto = AddressMapper.INSTANCE.toDto(address);

    // then
    assertEquals(dto.getId(), address.getId());
    assertEquals(dto.getStreetId(), address.getStreet().getId());
    assertEquals(dto.getHouseNumber(), address.getHouseNumber());
    assertEquals(dto.getPostalCode(), address.getPostalCode());
  }
}
