package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response;

public class ImageReferenceDto {

  private Long id;
  private String url;
  private String title;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
