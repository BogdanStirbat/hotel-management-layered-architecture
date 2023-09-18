package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response;


public class RoomTypeDto {

  private Long id;
  private Long hotelId;
  private String name;
  private String description;
  private Integer numberOfAvailableRooms;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
