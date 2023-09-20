package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;

public class FacilityGenerator {

  public static class FacilityCreateDtoBuilder {

    private String name;

    private FacilityCreateDtoBuilder() {

    }

    public static FacilityGenerator.FacilityCreateDtoBuilder builder() {
      return new FacilityGenerator.FacilityCreateDtoBuilder();
    }

    public FacilityGenerator.FacilityCreateDtoBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public FacilityCreateDto build() {
      FacilityCreateDto dto = new FacilityCreateDto();
      dto.setName(name);

      return dto;
    }
  }

  public static class FacilityBuilder {

    private Long id;
    private String name;

    private FacilityBuilder() {

    }

    public static FacilityGenerator.FacilityBuilder builder() {
      return new FacilityGenerator.FacilityBuilder();
    }

    public FacilityGenerator.FacilityBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public FacilityGenerator.FacilityBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public Facility build() {
      Facility roomType = new Facility();
      roomType.setId(id);
      roomType.setName(name);

      return roomType;
    }
  }
}
