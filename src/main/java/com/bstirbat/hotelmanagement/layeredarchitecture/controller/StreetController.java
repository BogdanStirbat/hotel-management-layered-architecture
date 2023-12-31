package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.STREETS;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.StreetMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.StreetDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
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
@RequestMapping(STREETS)
public class StreetController {

  private final StreetService streetService;

  @Autowired
  public StreetController(StreetService streetService) {
    this.streetService = streetService;
  }

  @PostMapping
  public ResponseEntity<StreetDto> create(@RequestBody StreetCreateDto createDto) {
    Street street = streetService.create(createDto);

    StreetDto dto = StreetMapper.INSTANCE.toDto(street);

    URI resourceLocation = fromPath(STREETS)
        .pathSegment("{id}")
        .buildAndExpand(street.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<StreetDto> getById(@PathVariable Long id) {
    Street street = streetService.getById(id);

    StreetDto dto = StreetMapper.INSTANCE.toDto(street);

    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<StreetDto>> findAll(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

    Page<StreetDto> streetDtos = streetService.findAll(pageable)
        .map(StreetMapper.INSTANCE::toDto);

    return ResponseEntity.ok(streetDtos);
  }
}
