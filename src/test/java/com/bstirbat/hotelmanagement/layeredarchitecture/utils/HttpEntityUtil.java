package com.bstirbat.hotelmanagement.layeredarchitecture.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpEntityUtil {

  private HttpEntityUtil() {

  }

  public static <T> HttpEntity<T> createHttpEntity(T body, String bearerAuth, MediaType contentType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(contentType);
    if (!StringUtils.isEmpty(bearerAuth)) {
      headers.setBearerAuth(bearerAuth);
    }

    return new HttpEntity<>(body, headers);
  }
}
