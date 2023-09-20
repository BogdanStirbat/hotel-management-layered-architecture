package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.FACILITIES;
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
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.FacilityDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ConstraintValidationErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.FacilityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.FacilityGenerator;
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

class FacilityControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private FacilityService facilityService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value(value="${local.server.port}")
  private int port;

  private AuthenticationHelper authenticationHelper;

  private String facilitiesUrl;
  private String adminAuthToken;

  @BeforeEach
  void init() {
    objectMapper.registerModule(new PageJacksonModule());

    String url = "http://localhost:" + port;
    facilitiesUrl = url + FACILITIES;
    authenticationHelper = new AuthenticationHelper(restTemplate, url);
    adminAuthToken = authenticationHelper.obtainAdminToken();
  }

  @Test
  void create() {
    // given
    FacilityCreateDto createDto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    // when
    HttpEntity<FacilityCreateDto> requestEntity = createHttpEntity(createDto, adminAuthToken, APPLICATION_JSON);
    ResponseEntity<FacilityDto> responseEntity = this.restTemplate.exchange(facilitiesUrl, HttpMethod.POST, requestEntity, FacilityDto.class);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());
    assertTrue(responseEntity.getHeaders().get(HttpHeaders.LOCATION).get(0).startsWith(FACILITIES));

    FacilityDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertNotNull(responseDto.getId());
    assertEquals(createDto.getName(), responseDto.getName());
  }

  @Test
  void create_whenInvalidDto() {
    // when
    HttpEntity<FacilityCreateDto> requestEntity = createHttpEntity(new FacilityCreateDto(), adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ConstraintValidationErrorDto> responseEntity = this.restTemplate.exchange(facilitiesUrl, HttpMethod.POST, requestEntity, ConstraintValidationErrorDto.class);

    // then
    assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

    ConstraintValidationErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(BAD_REQUEST.value(), errorDto.statusCode());
    assertEquals(1, errorDto.violationErrors().size());
    assertTrue(errorDto.message().contains("name"));
  }

  @Test
  void getById() {
    // given
    FacilityCreateDto createDto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    Facility imageReference = facilityService.create(createDto);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<FacilityDto> responseEntity = this.restTemplate.exchange(facilitiesUrl + "/" + imageReference.getId(), HttpMethod.GET, requestEntity, FacilityDto.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    FacilityDto responseDto = responseEntity.getBody();

    assertNotNull(responseDto);
    assertEquals(imageReference.getId(), responseDto.getId());
    assertEquals(imageReference.getName(), responseDto.getName());
  }

  @Test
  void getById_whenInvalidId() {
    // given
    long invalidId = 1L;

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ErrorDto> responseEntity = this.restTemplate.exchange(facilitiesUrl + "/" + invalidId, HttpMethod.GET, requestEntity, ErrorDto.class);

    // then
    assertEquals(NOT_FOUND, responseEntity.getStatusCode());

    ErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(NOT_FOUND.value(), errorDto.statusCode());
  }

  @Test
  void findAll() throws Exception {
    // given
    FacilityCreateDto createDto1 = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    FacilityCreateDto createDto2 = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Restaurant")
        .build();

    FacilityCreateDto createDto3 = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Terrace")
        .build();

    Facility facility1 = facilityService.create(createDto1);
    Facility facility2 = facilityService.create(createDto2);
    Facility facility3 = facilityService.create(createDto3);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(facilitiesUrl)
        .queryParam("page", 0)
        .queryParam("size", 2);

    ResponseEntity<String> firstResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    builder = UriComponentsBuilder.fromHttpUrl(facilitiesUrl)
        .queryParam("page", 1)
        .queryParam("size", 2);

    ResponseEntity<String> secondResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, firstResponseEntity.getStatusCode());

    Page<FacilityDto> firstResponseList = objectMapper.readValue(firstResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, firstResponseList.getTotalElements());
    assertEquals(2, firstResponseList.getNumberOfElements());
    assertEquals(facility1.getId(), firstResponseList.getContent().get(0).getId());
    assertEquals(facility2.getId(), firstResponseList.getContent().get(1).getId());

    assertEquals(OK, secondResponseEntity.getStatusCode());

    Page<FacilityDto> secondResponseList = objectMapper.readValue(secondResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, secondResponseList.getTotalElements());
    assertEquals(1, secondResponseList.getNumberOfElements());
    assertEquals(facility3.getId(), secondResponseList.getContent().get(0).getId());
  }

  @Test
  void findAll_whenNoFacilityExists() throws Exception {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(facilitiesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    Page<AddressDto> responseList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
    assertEquals(0, responseList.getTotalElements());
  }

  @Test
  void create_whenAnonymousUser() {
    // given
    FacilityCreateDto createDto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    // when
    HttpEntity<FacilityCreateDto> requestEntity = createHttpEntity(createDto, null, APPLICATION_JSON);
    ResponseEntity<FacilityDto> responseEntity = this.restTemplate.exchange(facilitiesUrl, HttpMethod.POST, requestEntity, FacilityDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<FacilityDto> responseEntity = this.restTemplate.exchange(facilitiesUrl + "/1", HttpMethod.GET, requestEntity, FacilityDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(facilitiesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenStaffUser() {
    // given
    FacilityCreateDto createDto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    // when
    HttpEntity<FacilityCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<FacilityDto> responseEntity = this.restTemplate.exchange(facilitiesUrl, HttpMethod.POST, requestEntity, FacilityDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<FacilityDto> responseEntity = this.restTemplate.exchange(facilitiesUrl + "/1", HttpMethod.GET, requestEntity, FacilityDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(facilitiesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenClientUser() {
    // given
    FacilityCreateDto createDto = FacilityGenerator.FacilityCreateDtoBuilder.builder()
        .withName("Free WiFi")
        .build();

    // when
    HttpEntity<FacilityCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<FacilityDto> responseEntity = this.restTemplate.exchange(facilitiesUrl, HttpMethod.POST, requestEntity, FacilityDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<FacilityDto> responseEntity = this.restTemplate.exchange(facilitiesUrl + "/1", HttpMethod.GET, requestEntity, FacilityDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(facilitiesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }
}
