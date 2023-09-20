package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.FACILITIES;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.FacilityMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.FacilityDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.FacilityService;
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
@RequestMapping(FACILITIES)
public class FacilityController {

  private final FacilityService facilityService;

  @Autowired
  public FacilityController(FacilityService facilityService) {
    this.facilityService = facilityService;
  }

  @PostMapping
  public ResponseEntity<FacilityDto> create(@RequestBody FacilityCreateDto createDto) {
    Facility facility = facilityService.create(createDto);

    FacilityDto dto = FacilityMapper.INSTANCE.toDto(facility);

    URI resourceLocation = fromPath(FACILITIES)
        .pathSegment("{id}")
        .buildAndExpand(facility.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<FacilityDto> getById(@PathVariable Long id) {
    Facility facility = facilityService.getById(id);

    FacilityDto dto = FacilityMapper.INSTANCE.toDto(facility);

    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<FacilityDto>> findAll(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

    Page<FacilityDto> facilityDtos = facilityService.findAll(pageable)
        .map(FacilityMapper.INSTANCE::toDto);

    return ResponseEntity.ok(facilityDtos);
  }
}
