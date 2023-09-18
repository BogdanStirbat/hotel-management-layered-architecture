package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.HOTELS;
import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.ROOM_TYPES;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.RoomTypeMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.RoomTypeDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.RoomTypeService;
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
@RequestMapping(ROOM_TYPES)
public class RoomTypeController {

  private final RoomTypeService roomTypeService;

  @Autowired
  public RoomTypeController(RoomTypeService roomTypeService) {
    this.roomTypeService = roomTypeService;
  }

  @PostMapping
  public ResponseEntity<RoomTypeDto> create(@RequestBody RoomTypeCreateDto createDto) {
    RoomType roomType = roomTypeService.create(createDto);

    RoomTypeDto dto = RoomTypeMapper.INSTANCE.toDto(roomType);

    URI resourceLocation = fromPath(HOTELS)
        .pathSegment("{id}")
        .buildAndExpand(roomType.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RoomTypeDto> getById(@PathVariable Long id) {
    RoomType roomType = roomTypeService.getById(id);

    RoomTypeDto dto = RoomTypeMapper.INSTANCE.toDto(roomType);

    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<RoomTypeDto>> findAll(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

    Page<RoomTypeDto> hotelDtos = roomTypeService.findAll(pageable)
        .map(RoomTypeMapper.INSTANCE::toDto);

    return ResponseEntity.ok(hotelDtos);
  }
}
