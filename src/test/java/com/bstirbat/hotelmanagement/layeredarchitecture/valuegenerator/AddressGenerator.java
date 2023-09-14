package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;

public class AddressGenerator {

  public static class AddressCreateDtoBuilder {

    private String houseNumber;
    private String postalCode;
    private Long streetId;

    private AddressCreateDtoBuilder() {

    }

    public static AddressGenerator.AddressCreateDtoBuilder builder() {
      return new AddressGenerator.AddressCreateDtoBuilder();
    }

    public AddressGenerator.AddressCreateDtoBuilder withHouseNumber(String houseNumber) {
      this.houseNumber = houseNumber;
      return this;
    }

    public AddressGenerator.AddressCreateDtoBuilder withPostalCode(String postalCode) {
      this.postalCode = postalCode;
      return this;
    }

    public AddressGenerator.AddressCreateDtoBuilder withStreetId(Long streetId) {
      this.streetId = streetId;
      return this;
    }

    public AddressCreateDto build() {
      AddressCreateDto dto = new AddressCreateDto();
      dto.setHouseNumber(houseNumber);
      dto.setPostalCode(postalCode);
      dto.setStreetId(streetId);

      return dto;
    }
  }

  public static class AddressBuilder {
    private Long id;
    private String houseNumber;
    private String postalCode;
    private Street street;

    private AddressBuilder() {

    }

    public static AddressGenerator.AddressBuilder builder() {
      return new AddressGenerator.AddressBuilder();
    }

    public AddressGenerator.AddressBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public AddressGenerator.AddressBuilder withHouseNumber(String houseNumber) {
      this.houseNumber = houseNumber;
      return this;
    }

    public AddressGenerator.AddressBuilder withPostalCode(String postalCode) {
      this.postalCode = postalCode;
      return this;
    }

    public AddressGenerator.AddressBuilder withStreet(Street street) {
      this.street = street;
      return this;
    }

    public Address build() {
      Address address = new Address();
      address.setId(id);
      address.setHouseNumber(houseNumber);
      address.setPostalCode(postalCode);
      address.setStreet(street);

      return address;
    }
  }
}
