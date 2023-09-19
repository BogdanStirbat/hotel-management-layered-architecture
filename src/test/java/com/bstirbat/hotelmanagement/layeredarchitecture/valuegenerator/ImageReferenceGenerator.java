package com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ImageReferenceCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.ImageReference;

public class ImageReferenceGenerator {

  public static class ImageReferenceCreateDtoBuilder {

    private String url;
    private String title;

    private ImageReferenceCreateDtoBuilder() {

    }

    public static ImageReferenceGenerator.ImageReferenceCreateDtoBuilder builder() {
      return new ImageReferenceCreateDtoBuilder();
    }

    public ImageReferenceGenerator.ImageReferenceCreateDtoBuilder withUrl(String url) {
      this.url = url;
      return this;
    }

    public ImageReferenceGenerator.ImageReferenceCreateDtoBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public ImageReferenceCreateDto build() {
      ImageReferenceCreateDto dto = new ImageReferenceCreateDto();
      dto.setUrl(url);
      dto.setTitle(title);

      return dto;
    }
  }

  public static class ImageReferenceBuilder {

    private Long id;
    private String url;
    private String title;

    private ImageReferenceBuilder() {

    }

    public static ImageReferenceGenerator.ImageReferenceBuilder builder() {
      return new ImageReferenceBuilder();
    }

    public ImageReferenceGenerator.ImageReferenceBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public ImageReferenceGenerator.ImageReferenceBuilder withUrl(String url) {
      this.url = url;
      return this;
    }

    public ImageReferenceGenerator.ImageReferenceBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public ImageReference build() {
      ImageReference roomType = new ImageReference();
      roomType.setId(id);
      roomType.setUrl(url);
      roomType.setTitle(title);

      return roomType;
    }
  }
}
