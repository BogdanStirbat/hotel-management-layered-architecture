package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;

public class CountryGenerator {

  public static class CountryCreateDtoBuilder {
    private String name;
    private String countryCode;

    private CountryCreateDtoBuilder() {

    }

    public static CountryCreateDtoBuilder builder() {
      return new CountryCreateDtoBuilder();
    }

    public CountryCreateDtoBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public CountryCreateDtoBuilder withCountryCode(String countryCode) {
      this.countryCode = countryCode;
      return this;
    }

    public CountryCreateDto build() {
      CountryCreateDto dto = new CountryCreateDto();
      dto.setName(name);
      dto.setCountryCode(countryCode);

      return dto;
    }
  }

  public static class CountryBuilder {
    private Long id;
    private String name;
    private String countryCode;

    private CountryBuilder() {

    }

    public static CountryBuilder builder() {
      return new CountryBuilder();
    }

    public CountryBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public CountryBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public CountryBuilder withCountryCode(String countryCode) {
      this.countryCode = countryCode;
      return this;
    }

    public Country build() {
      Country country = new Country();
      country.setId(id);
      country.setName(name);
      country.setCountryCode(countryCode);

      return country;
    }
  }


}
