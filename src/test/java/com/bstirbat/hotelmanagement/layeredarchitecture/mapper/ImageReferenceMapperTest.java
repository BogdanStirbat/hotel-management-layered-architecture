package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ImageReferenceCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ImageReferenceDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.ImageReference;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ImageReferenceGenerator;
import org.junit.jupiter.api.Test;

class ImageReferenceMapperTest {

  @Test
  void toEntity() {
    // given
    ImageReferenceCreateDto dto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    // when
    ImageReference entity = ImageReferenceMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertEquals(dto.getUrl(), entity.getUrl());
    assertEquals(dto.getTitle(), entity.getTitle());
  }

  @Test
  void toDto() {
    // given
    ImageReference imageReference = ImageReferenceGenerator.ImageReferenceBuilder.builder()
        .withId(1L)
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    // when
    ImageReferenceDto dto = ImageReferenceMapper.INSTANCE.toDto(imageReference);

    // then
    assertEquals(dto.getId(), imageReference.getId());
    assertEquals(dto.getUrl(), imageReference.getUrl());
    assertEquals(dto.getTitle(), imageReference.getTitle());
  }
}
