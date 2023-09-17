package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.HOTELS;
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
import com.bstirbat.hotelmanagement.layeredarchitecture.helper.AddressHelper;
import com.bstirbat.hotelmanagement.layeredarchitecture.helper.AuthenticationHelper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.HotelDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ConstraintValidationErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.HotelService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
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

class HotelControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CountryService countryService;

  @Autowired
  private CityService cityService;

  @Autowired
  private StreetService streetService;

  @Autowired
  private AddressService addressService;

  @Autowired
  private HotelService hotelService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value(value="${local.server.port}")
  private int port;

  private AuthenticationHelper authenticationHelper;

  private String hotelsUrl;
  private String adminAuthToken;

  private Address address;

  @BeforeEach
  void init() {
    objectMapper.registerModule(new PageJacksonModule());

    String url = "http://localhost:" + port;
    hotelsUrl = url + HOTELS;
    authenticationHelper = new AuthenticationHelper(restTemplate, url);
    adminAuthToken = authenticationHelper.obtainAdminToken();

    AddressHelper addressHelper = new AddressHelper(countryService, cityService, streetService, addressService);
    address = addressHelper.createAnAddress();
  }

  @Test
  void create() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    // when
    HttpEntity<HotelCreateDto> requestEntity = createHttpEntity(createDto, adminAuthToken, APPLICATION_JSON);
    ResponseEntity<HotelDto> responseEntity = this.restTemplate.exchange(hotelsUrl, HttpMethod.POST, requestEntity, HotelDto.class);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());
    assertTrue(responseEntity.getHeaders().get(HttpHeaders.LOCATION).get(0).startsWith(HOTELS));

    HotelDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertNotNull(responseDto.getId());
    assertEquals(createDto.getAddressId(), responseDto.getAddressId());
    assertEquals(createDto.getName(), responseDto.getName());
    assertEquals(createDto.getDescription(), responseDto.getDescription());
  }

  @Test
  void create_whenInvalidDto() {
    // when
    HttpEntity<HotelCreateDto> requestEntity = createHttpEntity(new HotelCreateDto(), adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ConstraintValidationErrorDto> responseEntity = this.restTemplate.exchange(hotelsUrl, HttpMethod.POST, requestEntity, ConstraintValidationErrorDto.class);

    // then
    assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

    ConstraintValidationErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(BAD_REQUEST.value(), errorDto.statusCode());
    assertEquals(3, errorDto.violationErrors().size());
    assertTrue(errorDto.message().contains("addressId"));
    assertTrue(errorDto.message().contains("name"));
    assertTrue(errorDto.message().contains("description"));
  }

  @Test
  void getById() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    Hotel hotel = hotelService.create(createDto);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<HotelDto> responseEntity = this.restTemplate.exchange(hotelsUrl + "/" + hotel.getId(), HttpMethod.GET, requestEntity, HotelDto.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    HotelDto responseDto = responseEntity.getBody();

    assertNotNull(responseDto);
    assertEquals(hotel.getId(), responseDto.getId());
    assertEquals(hotel.getAddress().getId(), responseDto.getAddressId());
    assertEquals(hotel.getName(), responseDto.getName());
    assertEquals(hotel.getDescription(), responseDto.getDescription());
  }

  @Test
  void getById_whenInvalidId() {
    // given
    long invalidId = 1L;

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ErrorDto> responseEntity = this.restTemplate.exchange(hotelsUrl + "/" + invalidId, HttpMethod.GET, requestEntity, ErrorDto.class);

    // then
    assertEquals(NOT_FOUND, responseEntity.getStatusCode());

    ErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(NOT_FOUND.value(), errorDto.statusCode());
  }

  @Test
  void findAll() throws Exception {
    // given
    HotelCreateDto createDto1 = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott 1")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    HotelCreateDto createDto2 = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott 2")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    HotelCreateDto createDto3 = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott 3")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    Hotel hotel1 = hotelService.create(createDto1);
    Hotel hotel2 = hotelService.create(createDto2);
    Hotel hotel3 = hotelService.create(createDto3);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hotelsUrl)
        .queryParam("page", 0)
        .queryParam("size", 2);

    ResponseEntity<String> firstResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    builder = UriComponentsBuilder.fromHttpUrl(hotelsUrl)
        .queryParam("page", 1)
        .queryParam("size", 2);

    ResponseEntity<String> secondResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, firstResponseEntity.getStatusCode());

    Page<HotelDto> firstResponseList = objectMapper.readValue(firstResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, firstResponseList.getTotalElements());
    assertEquals(2, firstResponseList.getNumberOfElements());
    assertEquals(hotel1.getId(), firstResponseList.getContent().get(0).getId());
    assertEquals(hotel2.getId(), firstResponseList.getContent().get(1).getId());

    assertEquals(OK, secondResponseEntity.getStatusCode());

    Page<HotelDto> secondResponseList = objectMapper.readValue(secondResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, secondResponseList.getTotalElements());
    assertEquals(1, secondResponseList.getNumberOfElements());
    assertEquals(hotel3.getId(), secondResponseList.getContent().get(0).getId());
  }

  @Test
  void findAll_whenNoHotelExists() throws Exception {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(hotelsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    Page<AddressDto> responseList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
    assertEquals(0, responseList.getTotalElements());
  }

  @Test
  void create_whenAnonymousUser() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    // when
    HttpEntity<HotelCreateDto> requestEntity = createHttpEntity(createDto, null, APPLICATION_JSON);
    ResponseEntity<HotelDto> responseEntity = this.restTemplate.exchange(hotelsUrl, HttpMethod.POST, requestEntity, HotelDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<HotelDto> responseEntity = this.restTemplate.exchange(hotelsUrl + "/1", HttpMethod.GET, requestEntity, HotelDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(hotelsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenStaffUser() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    // when
    HttpEntity<HotelCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<HotelDto> responseEntity = this.restTemplate.exchange(hotelsUrl, HttpMethod.POST, requestEntity, HotelDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<HotelDto> responseEntity = this.restTemplate.exchange(hotelsUrl + "/1", HttpMethod.GET, requestEntity, HotelDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(hotelsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenClientUser() {
    // given
    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();

    // when
    HttpEntity<HotelCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<HotelDto> responseEntity = this.restTemplate.exchange(hotelsUrl, HttpMethod.POST, requestEntity, HotelDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<HotelDto> responseEntity = this.restTemplate.exchange(hotelsUrl + "/1", HttpMethod.GET, requestEntity, HotelDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(hotelsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }
}
