package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.HOTELS;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.HotelMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.HotelDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.HotelService;
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
@RequestMapping(HOTELS)
public class HotelController {

  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @PostMapping
  public ResponseEntity<HotelDto> create(@RequestBody HotelCreateDto createDto) {
    Hotel hotel = hotelService.create(createDto);

    HotelDto dto = HotelMapper.INSTANCE.toDto(hotel);

    URI resourceLocation = fromPath(HOTELS)
        .pathSegment("{id}")
        .buildAndExpand(hotel.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<HotelDto> getById(@PathVariable Long id) {
    Hotel hotel = hotelService.getById(id);

    HotelDto dto = HotelMapper.INSTANCE.toDto(hotel);

    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<HotelDto>> findAll(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("houseNumber"));

    Page<HotelDto> hotelDtos = hotelService.findAll(pageable)
        .map(HotelMapper.INSTANCE::toDto);

    return ResponseEntity.ok(hotelDtos);
  }
}
