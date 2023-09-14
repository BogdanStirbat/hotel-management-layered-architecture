package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.ADDRESSES;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.AddressMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ADDRESSES)
public class AddressController {

  private final AddressService addressService;

  @Autowired
  public AddressController(AddressService addressService) {
    this.addressService = addressService;
  }

  @PostMapping
  public ResponseEntity<AddressDto> create(@RequestBody AddressCreateDto createDto) {
    Address address = addressService.create(createDto);

    AddressDto dto = AddressMapper.INSTANCE.toDto(address);

    URI resourceLocation = fromPath(ADDRESSES)
        .pathSegment("{id}")
        .buildAndExpand(address.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }
}
