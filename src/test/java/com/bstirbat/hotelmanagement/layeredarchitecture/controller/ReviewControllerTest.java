package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.enums.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.config.user.UserDetailsImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ReviewDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.ReviewService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ReviewGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.UserGenerator.UserBuilder;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

class ReviewControllerTest {

  private ReviewService reviewService;
  private ReviewController reviewController;

  private Booking booking;
  private User user;

  @BeforeEach
  public void setUp() {
    reviewService = mock(ReviewService.class);
    reviewController = new ReviewController(reviewService);

    booking = BookingGenerator.BookingBuilder.builder()
        .withId(9L)
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    user = UserBuilder.builder()
        .withId(2L)
        .withEmail("admin@test.com")
        .withRole(ADMIN)
        .build();
  }

  @Test
  void create() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
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

    when(reviewService.create(any(), any())).thenReturn(review);

    UserDetailsImpl userDetails = new UserDetailsImpl(user);
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);

    // when
    ResponseEntity<ReviewDto> responseEntity = reviewController.create(createDto, authentication);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    ReviewDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(review.getId(), responseDto.getId());
    assertEquals(review.getBooking().getId(), responseDto.getBookingId());
    assertEquals(review.getUser().getId(), responseDto.getUserId());
    assertEquals(review.getReviewDate(), responseDto.getReviewDate());
    assertEquals(review.getTitle(), responseDto.getTitle());
    assertEquals(review.getBody(), responseDto.getBody());
    assertEquals(review.getRating(), responseDto.getRating());
  }

  @Test
  void getById() {
    // given
    Review review = ReviewGenerator.ReviewBuilder.builder()
        .withId(3L)
        .withBooking(booking)
        .withUser(user)
        .withReviewDate(LocalDate.of(2023, Month.OCTOBER, 19))
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    when(reviewService.getById(review.getId())).thenReturn(review);

    // when
    ResponseEntity<ReviewDto> responseEntity = reviewController.getById(review.getId());

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    ReviewDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(review.getId(), responseDto.getId());
    assertEquals(review.getBooking().getId(), responseDto.getBookingId());
    assertEquals(review.getUser().getId(), responseDto.getUserId());
    assertEquals(review.getReviewDate(), responseDto.getReviewDate());
    assertEquals(review.getTitle(), responseDto.getTitle());
    assertEquals(review.getBody(), responseDto.getBody());
    assertEquals(review.getRating(), responseDto.getRating());
  }

  @Test
  void findAll() {
    // when
    when(reviewService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<ReviewDto>> responseEntity = reviewController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}