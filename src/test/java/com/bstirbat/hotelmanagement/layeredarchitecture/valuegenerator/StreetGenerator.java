package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;

public class StreetGenerator {

  public static class StreetCreateDtoBuilder {

    private String name;
    private Long cityId;

    private StreetCreateDtoBuilder() {

    }

    public static StreetGenerator.StreetCreateDtoBuilder builder() {
      return new StreetGenerator.StreetCreateDtoBuilder();
    }

    public StreetGenerator.StreetCreateDtoBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public StreetGenerator.StreetCreateDtoBuilder withCityId(Long cityId) {
      this.cityId = cityId;
      return this;
    }

    public StreetCreateDto build() {
      StreetCreateDto dto = new StreetCreateDto();
      dto.setName(name);
      dto.setCityId(cityId);

      return dto;
    }
  }

  public static class StreetBuilder {
    private Long id;
    private String name;
    private City city;

    private StreetBuilder() {

    }

    public static StreetGenerator.StreetBuilder builder() {
      return new StreetGenerator.StreetBuilder();
    }

    public StreetGenerator.StreetBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public StreetGenerator.StreetBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public StreetGenerator.StreetBuilder withCity(City city) {
      this.city = city;
      return this;
    }

    public Street build() {
      Street street = new Street();
      street.setId(id);
      street.setName(name);
      street.setCity(city);

      return street;
    }
  }
}
