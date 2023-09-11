package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.CITIES;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.CityMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CityDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CITIES)
public class CityController {

  private final CityService cityService;

  @Autowired
  public CityController(CityService cityService) {
    this.cityService = cityService;
  }

  @PostMapping
  public ResponseEntity<CityDto> create(@RequestBody CityCreateDto createDto) {
    City city = cityService.create(createDto);

    CityDto dto = CityMapper.INSTANCE.toDto(city);

    URI resourceLocation = fromPath(CITIES)
        .pathSegment("{id}")
        .buildAndExpand(city.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CityDto> getById(@PathVariable Long id) {
    City city = cityService.getById(id);

    CityDto dto = CityMapper.INSTANCE.toDto(city);

    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<CityDto>> findAll(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

    Page<CityDto> cityDtos = cityService.findAll(pageable)
        .map(CityMapper.INSTANCE::toDto);

    return ResponseEntity.ok(cityDtos);
  }
}
