package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request;

import jakarta.validation.constraints.NotNull;

public class ReviewCreateDto {

  @NotNull
  private Long bookingId;

  private String title;

  private String body;

  @NotNull
  private Integer rating;

  public Long getBookingId() {
    return bookingId;
  }

  public void setBookingId(Long bookingId) {
    this.bookingId = bookingId;
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
