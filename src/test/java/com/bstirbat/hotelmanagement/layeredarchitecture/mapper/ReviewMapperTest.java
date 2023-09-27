package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static com.bstirbat.hotelmanagement.layeredarchitecture.enums.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ReviewDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ReviewGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.UserGenerator.UserBuilder;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Test;

class ReviewMapperTest {

  @Test
  void toEntity() {
    // given
    ReviewCreateDto dto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(1L)
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    // when
    Review entity = ReviewMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertNull(entity.getBooking());
    assertNull(entity.getUser());
    assertNull(entity.getReviewDate());
    assertEquals(dto.getTitle(), entity.getTitle());
    assertEquals(dto.getBody(), entity.getBody());
    assertEquals(dto.getRating(), entity.getRating());
  }

  @Test
  void toDto() {
    // given
    User user = UserBuilder.builder()
        .withId(1L)
        .withEmail("admin@test.com")
        .withRole(ADMIN)
        .build();

    Booking booking = BookingGenerator.BookingBuilder.builder()
        .withId(2L)
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    Review review = ReviewGenerator.ReviewBuilder.builder()
        .withId(3L)
        .withBooking(booking)
        .withUser(user)
        .withReviewDate(LocalDate.of(2023, Month.OCTOBER, 19))
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    // when
    ReviewDto dto = ReviewMapper.INSTANCE.toDto(review);

    // then
    assertEquals(dto.getId(), review.getId());
    assertEquals(dto.getBookingId(), review.getBooking().getId());
    assertEquals(dto.getUserId(), review.getUser().getId());
    assertEquals(dto.getReviewDate(), review.getReviewDate());
    assertEquals(dto.getTitle(), review.getTitle());
    assertEquals(dto.getBody(), review.getBody());
    assertEquals(dto.getRating(), review.getRating());
  }
}