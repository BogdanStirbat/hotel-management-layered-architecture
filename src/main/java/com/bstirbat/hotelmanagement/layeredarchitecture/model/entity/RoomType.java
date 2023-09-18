package com.bstirbat.hotelmanagement.layeredarchitecture.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_type")
public class RoomType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "number_of_available_rooms", nullable = false)
  private Integer numberOfAvailableRooms;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hotel_id", nullable = false)
  private Hotel hotel;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Hotel getHotel() {
    return hotel;
  }

  public void setHotel(Hotel hotel) {
    this.hotel = hotel;
  }
}
