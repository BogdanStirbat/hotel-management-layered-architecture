package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response;

public class AddressDto {

  private Long id;
  private Long streetId;
  private String houseNumber;
  private String postalCode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getStreetId() {
    return streetId;
  }

  public void setStreetId(Long streetId) {
    this.streetId = streetId;
  }

  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
}
