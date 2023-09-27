package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response;

import java.time.LocalDate;

public class ReviewDto {

  private Long id;
  private Long bookingId;
  private Long userId;
  private LocalDate reviewDate;
  private String title;
  private String body;
  private Integer rating;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBookingId() {
    return bookingId;
  }

  public void setBookingId(Long bookingId) {
    this.bookingId = bookingId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public LocalDate getReviewDate() {
    return reviewDate;
  }

  public void setReviewDate(LocalDate reviewDate) {
    this.reviewDate = reviewDate;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }
}
