package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static com.bstirbat.hotelmanagement.layeredarchitecture.enums.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.ReviewRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.ReviewServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ReviewGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.UserGenerator.UserBuilder;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class ReviewServiceTest {

  private ReviewService reviewService;

  private BookingService bookingService;
  private ReviewRepository reviewRepository;

  private Booking booking;
  private User user;

  @BeforeEach
  public void setUp() {
    bookingService = mock(BookingService.class);
    reviewRepository = mock(ReviewRepository.class);

    reviewService = new ReviewServiceImpl(bookingService, reviewRepository);

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
    ReviewCreateDto dto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
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

    when(reviewRepository.save(any())).thenReturn(review);

    // when
    Review createdReview = reviewService.create(dto, user);

    // then
    assertEquals(review.getId(), createdReview.getId());
    assertEquals(review.getBooking().getId(), createdReview.getBooking().getId());
    assertEquals(review.getUser().getId(), createdReview.getUser().getId());
    assertEquals(review.getReviewDate(), createdReview.getReviewDate());
    assertEquals(review.getTitle(), createdReview.getTitle());
    assertEquals(review.getBody(), createdReview.getBody());
    assertEquals(review.getRating(), createdReview.getRating());
  }

  @Test
  void create_whenInvalidBookingId() {
    // given
    ReviewCreateDto dto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    when(bookingService.getById(booking.getId())).thenThrow(new ResourceNotFoundException("Could not find a booking with id 9"));

    // when & then
    assertThrows(InvalidDataException.class, () -> reviewService.create(dto, user));
  }

  @Test
  void getById() {
    // given
    Review review = ReviewGenerator.ReviewBuilder.builder()
        .withId(10L)
        .withBooking(booking)
        .withUser(user)
        .withReviewDate(LocalDate.of(2023, Month.OCTOBER, 19))
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

    // when
    Review retrievedReview = reviewService.getById(review.getId());

    // then
    assertEquals(review.getId(), retrievedReview.getId());
    assertEquals(review.getBooking().getId(), retrievedReview.getBooking().getId());
    assertEquals(review.getUser().getId(), retrievedReview.getUser().getId());
    assertEquals(review.getReviewDate(), retrievedReview.getReviewDate());
    assertEquals(review.getTitle(), retrievedReview.getTitle());
    assertEquals(review.getBody(), retrievedReview.getBody());
    assertEquals(review.getRating(), retrievedReview.getRating());
  }

  @Test
  void getById_whenNoReviewFound() {
    // given
    when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ResourceNotFoundException.class, () -> reviewService.getById(1L));
  }

  @Test
  void findAll() {
    // given
    Page<Review> page = Page.empty();

    when(reviewRepository.findAll(any(Pageable.class))).thenReturn(page);

    // when
    Page<Review> reviews = reviewService.findAll(Pageable.unpaged());

    // then
    assertTrue(reviews.isEmpty());
  }
}