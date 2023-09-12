package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class StreetCreateDto {

  @NotNull
  private Long cityId;

  @NotEmpty
  private String name;

  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
