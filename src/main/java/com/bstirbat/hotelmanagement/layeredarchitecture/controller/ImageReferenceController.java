package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.IMAGE_REFERENCES;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.ImageReferenceMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ImageReferenceCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ImageReferenceDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.ImageReference;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.ImageReferenceService;
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
@RequestMapping(IMAGE_REFERENCES)
public class ImageReferenceController {

  private final ImageReferenceService imageReferenceService;

  @Autowired
  public ImageReferenceController(ImageReferenceService imageReferenceService) {
    this.imageReferenceService = imageReferenceService;
  }

  @PostMapping
  public ResponseEntity<ImageReferenceDto> create(@RequestBody ImageReferenceCreateDto createDto) {
    ImageReference imageReference = imageReferenceService.create(createDto);

    ImageReferenceDto dto = ImageReferenceMapper.INSTANCE.toDto(imageReference);

    URI resourceLocation = fromPath(IMAGE_REFERENCES)
        .pathSegment("{id}")
        .buildAndExpand(imageReference.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ImageReferenceDto> getById(@PathVariable Long id) {
    ImageReference imageReference = imageReferenceService.getById(id);

    ImageReferenceDto dto = ImageReferenceMapper.INSTANCE.toDto(imageReference);

    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<ImageReferenceDto>> findAll(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page<ImageReferenceDto> imageReferenceDtos = imageReferenceService.findAll(pageable)
        .map(ImageReferenceMapper.INSTANCE::toDto);

    return ResponseEntity.ok(imageReferenceDtos);
  }
}
