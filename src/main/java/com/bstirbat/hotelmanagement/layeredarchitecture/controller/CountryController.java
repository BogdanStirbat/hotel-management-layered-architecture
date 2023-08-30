package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.CountryMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CountryDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
public class CountryController {

  private final CountryService countryService;

  @Autowired
  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @PostMapping
  public ResponseEntity<CountryDto> create(@RequestBody CountryCreateDto createDto) {
    Country country = countryService.create(createDto);

    CountryDto dto = CountryMapper.INSTANCE.toDto(country);

    return ResponseEntity.ok(dto);
  }
}
