package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constant.Paths.COUNTRIES;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.CountryMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CountryDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(COUNTRIES)
public class CountryController {

  private final CountryService countryService;

  @Autowired
  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<CountryDto> getById(@PathVariable Long id) {
    Country country = countryService.getById(id);

    CountryDto dto = CountryMapper.INSTANCE.toDto(country);

    return ResponseEntity.ok(dto);
  }

  @PostMapping
  public ResponseEntity<CountryDto> create(@RequestBody CountryCreateDto createDto) {
    Country country = countryService.create(createDto);

    CountryDto dto = CountryMapper.INSTANCE.toDto(country);

    URI resourceLocation = fromPath(COUNTRIES)
        .pathSegment("{id}")
        .buildAndExpand(country.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }
}
