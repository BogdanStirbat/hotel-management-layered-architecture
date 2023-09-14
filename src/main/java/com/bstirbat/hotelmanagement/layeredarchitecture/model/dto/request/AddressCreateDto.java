package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AddressCreateDto {

  @NotNull
  private Long streetId;

  @NotEmpty
  private String houseNumber;

  @NotEmpty
  private String postalCode;

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
