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

  @GetMapping("/{id}")
  public ResponseEntity<AddressDto> getById(@PathVariable Long id) {
    Address address = addressService.getById(id);

    AddressDto dto = AddressMapper.INSTANCE.toDto(address);

    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<AddressDto>> findAll(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

    Page<AddressDto> addressDtos = addressService.findAll(pageable)
        .map(AddressMapper.INSTANCE::toDto);

    return ResponseEntity.ok(addressDtos);
  }
}
