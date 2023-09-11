package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;

public class CityGenerator {

  public static class CityCreateDtoBuilder {

    private Long countryId;
    private String name;

    private CityCreateDtoBuilder() {

    }

    public static CityGenerator.CityCreateDtoBuilder builder() {
      return new CityGenerator.CityCreateDtoBuilder();
    }

    public CityGenerator.CityCreateDtoBuilder withCountryId(Long countryId) {
      this.countryId = countryId;
      return this;
    }

    public CityGenerator.CityCreateDtoBuilder withName(String name) {
      this.name = name;
      return this;
    }


    public CityCreateDto build() {
      CityCreateDto dto = new CityCreateDto();
      dto.setCountryId(countryId);
      dto.setName(name);

      return dto;
    }
  }

  public static class CityBuilder {
    private Long id;
    private Country country;
    private String name;

    private CityBuilder() {

    }

    public static CityGenerator.CityBuilder builder() {
      return new CityGenerator.CityBuilder();
    }

    public CityGenerator.CityBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public CityGenerator.CityBuilder withCountry(Country country) {
      this.country = country;
      return this;
    }

    public CityGenerator.CityBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public City build() {
      City city = new City();
      city.setId(id);
      city.setCountry(country);
      city.setName(name);

      return city;
    }
  }
}
