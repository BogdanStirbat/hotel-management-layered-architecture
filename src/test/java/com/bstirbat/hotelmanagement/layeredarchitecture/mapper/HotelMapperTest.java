package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.HotelDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.AddressGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import org.junit.jupiter.api.Test;

class HotelMapperTest {

  @Test
  void toEntity() {
    // given
    HotelCreateDto dto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(1L)
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    // when
    Hotel entity = HotelMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertNull(entity.getAddress());
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getDescription(), entity.getDescription());
  }

  @Test
  void toDto() {
    // given
    Address address = AddressGenerator.AddressBuilder.builder()
        .withId(1L)
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    Hotel hotel = HotelGenerator.HotelBuilder.builder()
        .withId(2L)
        .withAddress(address)
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    // when
    HotelDto dto = HotelMapper.INSTANCE.toDto(hotel);

    // then
    assertEquals(dto.getId(), hotel.getId());
    assertEquals(dto.getAddressId(), hotel.getAddress().getId());
    assertEquals(dto.getName(), hotel.getName());
    assertEquals(dto.getDescription(), hotel.getDescription());
  }
}