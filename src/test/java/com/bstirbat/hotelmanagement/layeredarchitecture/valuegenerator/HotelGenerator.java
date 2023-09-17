package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;

public class HotelGenerator {

  public static class HotelCreateDtoBuilder {

    private String name;
    private String description;
    private Long addressId;

    private HotelCreateDtoBuilder() {

    }

    public static HotelGenerator.HotelCreateDtoBuilder builder() {
      return new HotelGenerator.HotelCreateDtoBuilder();
    }

    public HotelGenerator.HotelCreateDtoBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public HotelGenerator.HotelCreateDtoBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    public HotelGenerator.HotelCreateDtoBuilder withAddressId(Long addressId) {
      this.addressId = addressId;
      return this;
    }

    public HotelCreateDto build() {
      HotelCreateDto dto = new HotelCreateDto();
      dto.setName(name);
      dto.setDescription(description);
      dto.setAddressId(addressId);

      return dto;
    }
  }

  public static class HotelBuilder {
    private Long id;
    private String name;
    private String description;
    private Address address;

    private HotelBuilder() {

    }

    public static HotelGenerator.HotelBuilder builder() {
      return new HotelGenerator.HotelBuilder();
    }

    public HotelGenerator.HotelBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public HotelGenerator.HotelBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public HotelGenerator.HotelBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    public HotelGenerator.HotelBuilder withAddress(Address address) {
      this.address = address;
      return this;
    }

    public Hotel build() {
      Hotel hotel = new Hotel();
      hotel.setId(id);
      hotel.setName(name);
      hotel.setDescription(description);
      hotel.setAddress(address);

      return hotel;
    }
  }
}
