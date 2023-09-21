package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.BookingCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import java.time.LocalDate;

public class BookingGenerator {

  public static class BookingCreateDtoBuilder {

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomTypeId;

    private BookingCreateDtoBuilder() {

    }

    public static BookingGenerator.BookingCreateDtoBuilder builder() {
      return new BookingGenerator.BookingCreateDtoBuilder();
    }

    public BookingGenerator.BookingCreateDtoBuilder withCheckInDate(LocalDate checkInDate) {
      this.checkInDate = checkInDate;
      return this;
    }

    public BookingGenerator.BookingCreateDtoBuilder withCheckOutDate(LocalDate checkOutDate) {
      this.checkOutDate = checkOutDate;
      return this;
    }

    public BookingGenerator.BookingCreateDtoBuilder withRoomTypeId(Long roomTypeId) {
      this.roomTypeId = roomTypeId;
      return this;
    }

    public BookingCreateDto build() {
      BookingCreateDto dto = new BookingCreateDto();
      dto.setCheckInDate(checkInDate);
      dto.setCheckOutDate(checkOutDate);
      dto.setRoomTypeId(roomTypeId);

      return dto;
    }
  }

  public static class BookingBuilder {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private RoomType roomType;
    private User user;

    private BookingBuilder() {

    }

    public static BookingGenerator.BookingBuilder builder() {
      return new BookingGenerator.BookingBuilder();
    }

    public BookingGenerator.BookingBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public BookingGenerator.BookingBuilder withCheckInDate(LocalDate checkInDate) {
      this.checkInDate = checkInDate;
      return this;
    }

    public BookingGenerator.BookingBuilder withCheckOutDate(LocalDate checkOutDate) {
      this.checkOutDate = checkOutDate;
      return this;
    }

    public BookingGenerator.BookingBuilder withRoomType(RoomType roomType) {
      this.roomType = roomType;
      return this;
    }

    public BookingGenerator.BookingBuilder withUser(User user) {
      this.user = user;
      return this;
    }

    public Booking build() {
      Booking booking = new Booking();
      booking.setId(id);
      booking.setCheckInDate(checkInDate);
      booking.setCheckOutDate(checkOutDate);
      booking.setRoomType(roomType);
      booking.setUser(user);

      return booking;
    }
  }
}
