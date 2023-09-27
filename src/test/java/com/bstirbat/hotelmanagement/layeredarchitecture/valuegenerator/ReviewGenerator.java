package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import java.time.LocalDate;

public class ReviewGenerator {

  public static class ReviewCreateDtoBuilder {

    private String title;
    private String body;
    private Integer rating;
    private Long bookingId;

    private ReviewCreateDtoBuilder() {

    }

    public static ReviewGenerator.ReviewCreateDtoBuilder builder() {
      return new ReviewGenerator.ReviewCreateDtoBuilder();
    }

    public ReviewGenerator.ReviewCreateDtoBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public ReviewGenerator.ReviewCreateDtoBuilder withBody(String body) {
      this.body = body;
      return this;
    }

    public ReviewGenerator.ReviewCreateDtoBuilder withRating(Integer rating) {
      this.rating = rating;
      return this;
    }

    public ReviewGenerator.ReviewCreateDtoBuilder withBookingId(Long bookingId) {
      this.bookingId = bookingId;
      return this;
    }

    public ReviewCreateDto build() {
      ReviewCreateDto dto = new ReviewCreateDto();
      dto.setTitle(title);
      dto.setBody(body);
      dto.setRating(rating);
      dto.setBookingId(bookingId);

      return dto;
    }
  }

  public static class ReviewBuilder {
    private Long id;
    private LocalDate reviewDate;
    private String title;
    private String body;
    private Integer rating;
    private Booking booking;
    private User user;

    private ReviewBuilder() {

    }

    public static ReviewGenerator.ReviewBuilder builder() {
      return new ReviewGenerator.ReviewBuilder();
    }

    public ReviewGenerator.ReviewBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public ReviewGenerator.ReviewBuilder withReviewDate(LocalDate reviewDate) {
      this.reviewDate = reviewDate;
      return this;
    }

    public ReviewGenerator.ReviewBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public ReviewGenerator.ReviewBuilder withBody(String body) {
      this.body = body;
      return this;
    }

    public ReviewGenerator.ReviewBuilder withRating(Integer rating) {
      this.rating = rating;
      return this;
    }

    public ReviewGenerator.ReviewBuilder withBooking(Booking booking) {
      this.booking = booking;
      return this;
    }

    public ReviewGenerator.ReviewBuilder withUser(User user) {
      this.user = user;
      return this;
    }

    public Review build() {
      Review review = new Review();
      review.setId(id);
      review.setReviewDate(reviewDate);
      review.setTitle(title);
      review.setBody(body);
      review.setRating(rating);
      review.setBooking(booking);
      review.setUser(user);

      return review;
    }
  }
}
