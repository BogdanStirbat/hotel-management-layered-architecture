package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CountryCreateDto {

  @NotEmpty
  private String name;

  @NotEmpty
  @Size(min = 2, max = 2)
  private String countryCode;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }
}
