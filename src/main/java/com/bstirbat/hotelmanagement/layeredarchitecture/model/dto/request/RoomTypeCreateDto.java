package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RoomTypeCreateDto {

  @NotNull
  private Long hotelId;

  @NotEmpty
  private String name;

  @NotEmpty
  private String description;

  @NotNull
  private Integer numberOfAvailableRooms;

  public Long getHotelId() {
    return hotelId;
  }

  public void setHotelId(Long hotelId) {
    this.hotelId = hotelId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getNumberOfAvailableRooms() {
    return numberOfAvailableRooms;
  }

  public void setNumberOfAvailableRooms(Integer numberOfAvailableRooms) {
    this.numberOfAvailableRooms = numberOfAvailableRooms;
  }
}
