package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.IMAGE_REFERENCES;
import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.HttpEntityUtil.createHttpEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.PageJacksonModule;
import com.bstirbat.hotelmanagement.layeredarchitecture.helper.AuthenticationHelper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ImageReferenceCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ImageReferenceDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ConstraintValidationErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.ImageReference;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.ImageReferenceService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ImageReferenceGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

class ImageReferenceControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ImageReferenceService imageReferenceService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value(value="${local.server.port}")
  private int port;

  private AuthenticationHelper authenticationHelper;

  private String imageReferencesUrl;
  private String adminAuthToken;

  @BeforeEach
  void init() {
    objectMapper.registerModule(new PageJacksonModule());

    String url = "http://localhost:" + port;
    imageReferencesUrl = url + IMAGE_REFERENCES;
    authenticationHelper = new AuthenticationHelper(restTemplate, url);
    adminAuthToken = authenticationHelper.obtainAdminToken();
  }

  @Test
  void create() {
    // given
    ImageReferenceCreateDto createDto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    // when
    HttpEntity<ImageReferenceCreateDto> requestEntity = createHttpEntity(createDto, adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ImageReferenceDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl, HttpMethod.POST, requestEntity, ImageReferenceDto.class);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());
    assertTrue(responseEntity.getHeaders().get(HttpHeaders.LOCATION).get(0).startsWith(IMAGE_REFERENCES));

    ImageReferenceDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertNotNull(responseDto.getId());
    assertEquals(createDto.getUrl(), responseDto.getUrl());
    assertEquals(createDto.getTitle(), responseDto.getTitle());
  }

  @Test
  void create_whenInvalidDto() {
    // when
    HttpEntity<ImageReferenceCreateDto> requestEntity = createHttpEntity(new ImageReferenceCreateDto(), adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ConstraintValidationErrorDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl, HttpMethod.POST, requestEntity, ConstraintValidationErrorDto.class);

    // then
    assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

    ConstraintValidationErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(BAD_REQUEST.value(), errorDto.statusCode());
    assertEquals(1, errorDto.violationErrors().size());
    assertTrue(errorDto.message().contains("url"));
  }

  @Test
  void getById() {
    // given
    ImageReferenceCreateDto createDto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    ImageReference imageReference = imageReferenceService.create(createDto);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ImageReferenceDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl + "/" + imageReference.getId(), HttpMethod.GET, requestEntity, ImageReferenceDto.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    ImageReferenceDto responseDto = responseEntity.getBody();

    assertNotNull(responseDto);
    assertEquals(imageReference.getId(), responseDto.getId());
    assertEquals(imageReference.getUrl(), responseDto.getUrl());
    assertEquals(imageReference.getTitle(), responseDto.getTitle());
  }

  @Test
  void getById_whenInvalidId() {
    // given
    long invalidId = 1L;

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ErrorDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl + "/" + invalidId, HttpMethod.GET, requestEntity, ErrorDto.class);

    // then
    assertEquals(NOT_FOUND, responseEntity.getStatusCode());

    ErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(NOT_FOUND.value(), errorDto.statusCode());
  }

  @Test
  void findAll() throws Exception {
    // given
    ImageReferenceCreateDto createDto1 = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel 1")
        .build();

    ImageReferenceCreateDto createDto2 = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel 2")
        .build();

    ImageReferenceCreateDto createDto3 = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel 3")
        .build();

    ImageReference imageReference1 = imageReferenceService.create(createDto1);
    ImageReference imageReference2 = imageReferenceService.create(createDto2);
    ImageReference imageReference3 = imageReferenceService.create(createDto3);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(imageReferencesUrl)
        .queryParam("page", 0)
        .queryParam("size", 2);

    ResponseEntity<String> firstResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    builder = UriComponentsBuilder.fromHttpUrl(imageReferencesUrl)
        .queryParam("page", 1)
        .queryParam("size", 2);

    ResponseEntity<String> secondResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, firstResponseEntity.getStatusCode());

    Page<ImageReferenceDto> firstResponseList = objectMapper.readValue(firstResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, firstResponseList.getTotalElements());
    assertEquals(2, firstResponseList.getNumberOfElements());
    assertEquals(imageReference1.getId(), firstResponseList.getContent().get(0).getId());
    assertEquals(imageReference2.getId(), firstResponseList.getContent().get(1).getId());

    assertEquals(OK, secondResponseEntity.getStatusCode());

    Page<ImageReferenceDto> secondResponseList = objectMapper.readValue(secondResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, secondResponseList.getTotalElements());
    assertEquals(1, secondResponseList.getNumberOfElements());
    assertEquals(imageReference3.getId(), secondResponseList.getContent().get(0).getId());
  }

  @Test
  void findAll_whenNoImageReferenceExists() throws Exception {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(imageReferencesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    Page<AddressDto> responseList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
    assertEquals(0, responseList.getTotalElements());
  }

  @Test
  void create_whenAnonymousUser() {
    // given
    ImageReferenceCreateDto createDto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    // when
    HttpEntity<ImageReferenceCreateDto> requestEntity = createHttpEntity(createDto, null, APPLICATION_JSON);
    ResponseEntity<ImageReferenceDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl, HttpMethod.POST, requestEntity, ImageReferenceDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<ImageReferenceDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl + "/1", HttpMethod.GET, requestEntity, ImageReferenceDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(imageReferencesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenStaffUser() {
    // given
    ImageReferenceCreateDto createDto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    // when
    HttpEntity<ImageReferenceCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<ImageReferenceDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl, HttpMethod.POST, requestEntity, ImageReferenceDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<ImageReferenceDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl + "/1", HttpMethod.GET, requestEntity, ImageReferenceDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(imageReferencesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenClientUser() {
    // given
    ImageReferenceCreateDto createDto = ImageReferenceGenerator.ImageReferenceCreateDtoBuilder.builder()
        .withUrl("https://something.static.com/images/max1024x768/436964190.jpg")
        .withTitle("a restaurant with charis and a bar at Marriott Hotel")
        .build();

    // when
    HttpEntity<ImageReferenceCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<ImageReferenceDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl, HttpMethod.POST, requestEntity, ImageReferenceDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<ImageReferenceDto> responseEntity = this.restTemplate.exchange(imageReferencesUrl + "/1", HttpMethod.GET, requestEntity, ImageReferenceDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(imageReferencesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }
}
