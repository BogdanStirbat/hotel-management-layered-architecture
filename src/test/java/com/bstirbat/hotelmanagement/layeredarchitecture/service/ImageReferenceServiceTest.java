package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ImageReferenceCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.ImageReference;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.ImageReferenceRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.ImageReferenceServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ImageReferenceGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class ImageReferenceServiceTest {

  private ImageReferenceService imageReferenceService;

  private ImageReferenceRepository imageReferenceRepository;


  @BeforeEach
  public void setUp() {
    imageReferenceRepository = mock(ImageReferenceRepository.class);
    imageReferenceService = new ImageReferenceServiceImpl(imageReferenceRepository);
  }

  @Test
  void create() {
    // given
    ImageReferenceCreateDto dto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    ImageReference imageReference = ImageReferenceGenerator.ImageReferenceBuilder.builder()
        .withId(1L)
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    when(imageReferenceRepository.save(any())).thenReturn(imageReference);

    // when
    ImageReference createdImageReference = imageReferenceService.create(dto);

    // then
    assertEquals(imageReference.getId(), createdImageReference.getId());
    assertEquals(imageReference.getUrl(), createdImageReference.getUrl());
    assertEquals(imageReference.getTitle(), createdImageReference.getTitle());
  }

  @Test
  void getById() {
    // given
    ImageReference imageReference = ImageReferenceGenerator.ImageReferenceBuilder.builder()
        .withId(1L)
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    when(imageReferenceRepository.findById(imageReference.getId())).thenReturn(Optional.of(imageReference));

    // when
    ImageReference retrievedImageReference = imageReferenceService.getById(imageReference.getId());

    // then
    assertEquals(imageReference.getId(), retrievedImageReference.getId());
    assertEquals(imageReference.getUrl(), retrievedImageReference.getUrl());
    assertEquals(imageReference.getTitle(), retrievedImageReference.getTitle());
  }

  @Test
  void findAll() {
    // given
    Page<ImageReference> page = Page.empty();

    when(imageReferenceRepository.findAll(any(Pageable.class))).thenReturn(page);

    // when
    Page<ImageReference> roomTypes = imageReferenceService.findAll(Pageable.unpaged());

    // then
    assertTrue(roomTypes.isEmpty());
  }
}
