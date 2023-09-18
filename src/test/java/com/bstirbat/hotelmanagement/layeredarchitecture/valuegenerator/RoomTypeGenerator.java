package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;

public class RoomTypeGenerator {

  public static class RoomTypeCreateDtoBuilder {

    private String name;
    private String description;
    private Integer numberOfAvailableRooms;
    private Long hotelId;

    private RoomTypeCreateDtoBuilder() {

    }

    public static RoomTypeGenerator.RoomTypeCreateDtoBuilder builder() {
      return new RoomTypeGenerator.RoomTypeCreateDtoBuilder();
    }

    public RoomTypeGenerator.RoomTypeCreateDtoBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public RoomTypeGenerator.RoomTypeCreateDtoBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    public RoomTypeGenerator.RoomTypeCreateDtoBuilder withNumberOfAvailableRooms(Integer numberOfAvailableRooms) {
      this.numberOfAvailableRooms = numberOfAvailableRooms;
      return this;
    }

    public RoomTypeGenerator.RoomTypeCreateDtoBuilder withHotelId(Long hotelId) {
      this.hotelId = hotelId;
      return this;
    }

    public RoomTypeCreateDto build() {
      RoomTypeCreateDto dto = new RoomTypeCreateDto();
      dto.setName(name);
      dto.setDescription(description);
      dto.setNumberOfAvailableRooms(numberOfAvailableRooms);
      dto.setHotelId(hotelId);

      return dto;
    }
  }

  public static class RoomTypeBuilder {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfAvailableRooms;
    private Hotel hotel;

    private RoomTypeBuilder() {

    }

    public static RoomTypeGenerator.RoomTypeBuilder builder() {
      return new RoomTypeGenerator.RoomTypeBuilder();
    }

    public RoomTypeGenerator.RoomTypeBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public RoomTypeGenerator.RoomTypeBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public RoomTypeGenerator.RoomTypeBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    public RoomTypeGenerator.RoomTypeBuilder withNumberOfAvailableRooms(Integer numberOfAvailableRooms) {
      this.numberOfAvailableRooms = numberOfAvailableRooms;
      return this;
    }

    public RoomTypeGenerator.RoomTypeBuilder withHotel(Hotel hotel) {
      this.hotel = hotel;
      return this;
    }

    public RoomType build() {
      RoomType roomType = new RoomType();
      roomType.setId(id);
      roomType.setName(name);
      roomType.setDescription(description);
      roomType.setNumberOfAvailableRooms(numberOfAvailableRooms);
      roomType.setHotel(hotel);

      return roomType;
    }
  }
}
