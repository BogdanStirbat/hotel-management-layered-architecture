package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static com.bstirbat.hotelmanagement.layeredarchitecture.enums.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.BookingCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.BookingDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.UserGenerator.UserBuilder;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Test;

class BookingMapperTest {

  @Test
  void toEntity() {
    // given
    BookingCreateDto dto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(1L)
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    // when
    Booking entity = BookingMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertNull(entity.getRoomType());
    assertNull(entity.getUser());
    assertEquals(dto.getCheckInDate(), entity.getCheckInDate());
    assertEquals(dto.getCheckOutDate(), entity.getCheckOutDate());
  }

  @Test
  void toDto() {
    // given
    RoomType roomType = RoomTypeGenerator.RoomTypeBuilder.builder()
        .withId(1L)
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    User user = UserBuilder.builder()
        .withId(2L)
        .withEmail("admin@test.com")
        .withRole(ADMIN)
        .build();

    Booking booking = BookingGenerator.BookingBuilder.builder()
        .withId(3L)
        .withRoomType(roomType)
        .withUser(user)
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    // when
    BookingDto dto = BookingMapper.INSTANCE.toDto(booking);

    // then
    assertEquals(dto.getId(), booking.getId());
    assertEquals(dto.getRoomTypeId(), booking.getRoomType().getId());
    assertEquals(dto.getUserId(), booking.getUser().getId());
    assertEquals(dto.getCheckInDate(), booking.getCheckInDate());
    assertEquals(dto.getCheckOutDate(), booking.getCheckOutDate());
  }
}
