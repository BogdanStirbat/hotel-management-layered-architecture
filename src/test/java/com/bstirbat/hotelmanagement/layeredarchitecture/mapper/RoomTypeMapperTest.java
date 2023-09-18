package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.RoomTypeDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import org.junit.jupiter.api.Test;

class RoomTypeMapperTest {

  @Test
  void toEntity() {
    // given
    RoomTypeCreateDto dto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(1L)
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    // when
    RoomType entity = RoomTypeMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertNull(entity.getHotel());
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getDescription(), entity.getDescription());
    assertEquals(dto.getNumberOfAvailableRooms(), entity.getNumberOfAvailableRooms());
  }

  @Test
  void toDto() {
    // given
    Hotel hotel = HotelGenerator.HotelBuilder.builder()
        .withId(1L)
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    RoomType roomType = RoomTypeGenerator.RoomTypeBuilder.builder()
        .withId(2L)
        .withHotel(hotel)
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    // when
    RoomTypeDto dto = RoomTypeMapper.INSTANCE.toDto(roomType);

    // then
    assertEquals(dto.getId(), roomType.getId());
    assertEquals(dto.getHotelId(), roomType.getHotel().getId());
    assertEquals(dto.getName(), roomType.getName());
    assertEquals(dto.getDescription(), roomType.getDescription());
    assertEquals(dto.getNumberOfAvailableRooms(), roomType.getNumberOfAvailableRooms());
  }
}