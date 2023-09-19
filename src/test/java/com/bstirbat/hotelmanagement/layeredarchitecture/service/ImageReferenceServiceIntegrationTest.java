package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ImageReferenceCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.ImageReference;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ImageReferenceGenerator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class ImageReferenceServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private ImageReferenceService imageReferenceService;

  @Test
  void create() {
    // given
    ImageReferenceCreateDto createDto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    // when
    ImageReference imageReference = imageReferenceService.create(createDto);

    // then
    assertNotNull(imageReference.getId());
    assertEquals(createDto.getUrl(), imageReference.getUrl());
    assertEquals(createDto.getTitle(), imageReference.getTitle());
  }



  @Test
  void create_whenInvalidDto() {
    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> imageReferenceService.create(new ImageReferenceCreateDto()));

    // then
    assertTrue(ex.getMessage().contains("url"));
  }

  @Test
  void create_whenNullDto() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> imageReferenceService.create(null));
  }

  @Test
  void getByd() {
    // given
    ImageReferenceCreateDto createDto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    ImageReference imageReference = imageReferenceService.create(createDto);

    // when
    ImageReference retrievedImageReference = imageReferenceService.getById(imageReference.getId());

    // then
    assertEquals(imageReference.getId(), retrievedImageReference.getId());
    assertEquals(imageReference.getUrl(), retrievedImageReference.getUrl());
    assertEquals(imageReference.getTitle(), retrievedImageReference.getTitle());
  }

  @Test
  void getByd_whenNoRoomTypeFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> imageReferenceService.getById(-1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> imageReferenceService.getById(null));
  }

  @Test
  void findAll() {
    // given
    ImageReferenceCreateDto createDto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    ImageReference imageReference = imageReferenceService.create(createDto);

    // when
    Page<ImageReference> imageReferences = imageReferenceService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  imageReferences.getTotalElements());

    ImageReference foundImageReference = imageReferences.getContent().get(0);
    assertEquals(imageReference.getId(), foundImageReference.getId());
    assertEquals(imageReference.getUrl(), foundImageReference.getUrl());
    assertEquals(imageReference.getTitle(), foundImageReference.getTitle());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> imageReferenceService.findAll(null));
  }
}
