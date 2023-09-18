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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
