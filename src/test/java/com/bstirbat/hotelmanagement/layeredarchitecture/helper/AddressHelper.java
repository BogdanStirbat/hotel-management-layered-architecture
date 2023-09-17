package com.bstirbat.hotelmanagement.layeredarchitecture.helper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.AddressGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator.StreetCreateDtoBuilder;

public class AddressHelper {

  private final CountryService countryService;
  private final CityService cityService;
  private final StreetService streetService;
  private final AddressService addressService;

  public AddressHelper(CountryService countryService, CityService cityService, StreetService streetService,
      AddressService addressService) {
    this.countryService = countryService;
    this.cityService = cityService;
    this.streetService = streetService;
    this.addressService = addressService;
  }

  public Address createAnAddress() {
    CountryCreateDto countryCreateDto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE")
        .build();
    Country country = countryService.create(countryCreateDto);

    CityCreateDto cityCreateDto = CityGenerator.CityCreateDtoBuilder.builder()
        .withCountryId(country.getId())
        .withName("Berlin")
        .build();
    City city = cityService.create(cityCreateDto);

    StreetCreateDto createDto = StreetCreateDtoBuilder.builder()
        .withCityId(city.getId())
        .withName("Street")
        .build();
    Street street = streetService.create(createDto);

    AddressCreateDto addressCreateDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    return addressService.create(addressCreateDto);
  }
}
