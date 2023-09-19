package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ImageReferenceCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ImageReferenceDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.ImageReference;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.ImageReferenceService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ImageReferenceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

class ImageReferenceControllerTest {

  private ImageReferenceService imageReferenceService;

  private ImageReferenceController imageReferenceController;

  @BeforeEach
  public void setUp() {
    imageReferenceService = mock(ImageReferenceService.class);
    imageReferenceController = new ImageReferenceController(imageReferenceService);
  }

  @Test
  void create() {
    // given
    ImageReferenceCreateDto createDto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    ImageReference imageReference = ImageReferenceGenerator.ImageReferenceBuilder.builder()
        .withId(1L)
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    when(imageReferenceService.create(any())).thenReturn(imageReference);

    // when
    ResponseEntity<ImageReferenceDto> responseEntity = imageReferenceController.create(createDto);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    ImageReferenceDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(imageReference.getId(), responseDto.getId());
    assertEquals(imageReference.getUrl(), responseDto.getUrl());
    assertEquals(imageReference.getTitle(), responseDto.getTitle());
  }

  @Test
  void getById() {
    // given
    ImageReference imageReference = ImageReferenceGenerator.ImageReferenceBuilder.builder()
        .withId(1L)
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    when(imageReferenceService.getById(imageReference.getId())).thenReturn(imageReference);

    // when
    ResponseEntity<ImageReferenceDto> responseEntity = imageReferenceController.getById(imageReference.getId());

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    ImageReferenceDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(imageReference.getId(), responseDto.getId());
    assertEquals(imageReference.getUrl(), responseDto.getUrl());
    assertEquals(imageReference.getTitle(), responseDto.getTitle());
  }

  @Test
  void findAll() {
    // when
    when(imageReferenceService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<ImageReferenceDto>> responseEntity = imageReferenceController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
