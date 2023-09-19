package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.ROOM_TYPES;
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
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.RoomTypeDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ConstraintValidationErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.HotelService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.RoomTypeService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
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

class RoomTypeControllerIntegrationTest extends AbstractIntegrationTest {

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
  private RoomTypeService roomTypeService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value(value="${local.server.port}")
  private int port;

  private AuthenticationHelper authenticationHelper;

  private String roomTypesUrl;
  private String adminAuthToken;

  private Hotel hotel;

  @BeforeEach
  void init() {
    objectMapper.registerModule(new PageJacksonModule());

    String url = "http://localhost:" + port;
    roomTypesUrl = url + ROOM_TYPES;
    authenticationHelper = new AuthenticationHelper(restTemplate, url);
    adminAuthToken = authenticationHelper.obtainAdminToken();

    AddressHelper addressHelper = new AddressHelper(countryService, cityService, streetService, addressService);
    Address address = addressHelper.createAnAddress();

    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();
    hotel = hotelService.create(createDto);
  }

  @Test
  void create() {
    // given
    RoomTypeCreateDto createDto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    // when
    HttpEntity<RoomTypeCreateDto> requestEntity = createHttpEntity(createDto, adminAuthToken, APPLICATION_JSON);
    ResponseEntity<RoomTypeDto> responseEntity = this.restTemplate.exchange(roomTypesUrl, HttpMethod.POST, requestEntity, RoomTypeDto.class);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());
    assertTrue(responseEntity.getHeaders().get(HttpHeaders.LOCATION).get(0).startsWith(ROOM_TYPES));

    RoomTypeDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertNotNull(responseDto.getId());
    assertEquals(createDto.getHotelId(), responseDto.getHotelId());
    assertEquals(createDto.getName(), responseDto.getName());
    assertEquals(createDto.getDescription(), responseDto.getDescription());
    assertEquals(createDto.getNumberOfAvailableRooms(), responseDto.getNumberOfAvailableRooms());
  }

  @Test
  void create_whenInvalidDto() {
    // when
    HttpEntity<RoomTypeCreateDto> requestEntity = createHttpEntity(new RoomTypeCreateDto(), adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ConstraintValidationErrorDto> responseEntity = this.restTemplate.exchange(roomTypesUrl, HttpMethod.POST, requestEntity, ConstraintValidationErrorDto.class);

    // then
    assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

    ConstraintValidationErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(BAD_REQUEST.value(), errorDto.statusCode());
    assertEquals(4, errorDto.violationErrors().size());
    assertTrue(errorDto.message().contains("hotelId"));
    assertTrue(errorDto.message().contains("name"));
    assertTrue(errorDto.message().contains("description"));
    assertTrue(errorDto.message().contains("numberOfAvailableRooms"));
  }

  @Test
  void getById() {
    // given
    RoomTypeCreateDto createDto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    RoomType roomType = roomTypeService.create(createDto);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<RoomTypeDto> responseEntity = this.restTemplate.exchange(roomTypesUrl + "/" + roomType.getId(), HttpMethod.GET, requestEntity, RoomTypeDto.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    RoomTypeDto responseDto = responseEntity.getBody();

    assertNotNull(responseDto);
    assertEquals(roomType.getId(), responseDto.getId());
    assertEquals(roomType.getHotel().getId(), responseDto.getHotelId());
    assertEquals(roomType.getName(), responseDto.getName());
    assertEquals(roomType.getDescription(), responseDto.getDescription());
    assertEquals(roomType.getNumberOfAvailableRooms(), responseDto.getNumberOfAvailableRooms());
  }

  @Test
  void getById_whenInvalidId() {
    // given
    long invalidId = 1L;

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ErrorDto> responseEntity = this.restTemplate.exchange(roomTypesUrl + "/" + invalidId, HttpMethod.GET, requestEntity, ErrorDto.class);

    // then
    assertEquals(NOT_FOUND, responseEntity.getStatusCode());

    ErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(NOT_FOUND.value(), errorDto.statusCode());
  }

  @Test
  void findAll() throws Exception {
    // given
    RoomTypeCreateDto createDto1 = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    RoomTypeCreateDto createDto2 = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    RoomTypeCreateDto createDto3 = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    RoomType roomType1 = roomTypeService.create(createDto1);
    RoomType roomType2 = roomTypeService.create(createDto2);
    RoomType roomType3 = roomTypeService.create(createDto3);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(roomTypesUrl)
        .queryParam("page", 0)
        .queryParam("size", 2);

    ResponseEntity<String> firstResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    builder = UriComponentsBuilder.fromHttpUrl(roomTypesUrl)
        .queryParam("page", 1)
        .queryParam("size", 2);

    ResponseEntity<String> secondResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, firstResponseEntity.getStatusCode());

    Page<RoomTypeDto> firstResponseList = objectMapper.readValue(firstResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, firstResponseList.getTotalElements());
    assertEquals(2, firstResponseList.getNumberOfElements());
    assertEquals(roomType1.getId(), firstResponseList.getContent().get(0).getId());
    assertEquals(roomType2.getId(), firstResponseList.getContent().get(1).getId());

    assertEquals(OK, secondResponseEntity.getStatusCode());

    Page<RoomTypeDto> secondResponseList = objectMapper.readValue(secondResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, secondResponseList.getTotalElements());
    assertEquals(1, secondResponseList.getNumberOfElements());
    assertEquals(roomType3.getId(), secondResponseList.getContent().get(0).getId());
  }

  @Test
  void findAll_whenNoImageReferenceExists() throws Exception {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(roomTypesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    Page<AddressDto> responseList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
    assertEquals(0, responseList.getTotalElements());
  }

  @Test
  void create_whenAnonymousUser() {
    // given
    RoomTypeCreateDto createDto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    // when
    HttpEntity<RoomTypeCreateDto> requestEntity = createHttpEntity(createDto, null, APPLICATION_JSON);
    ResponseEntity<RoomTypeDto> responseEntity = this.restTemplate.exchange(roomTypesUrl, HttpMethod.POST, requestEntity, RoomTypeDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<RoomTypeDto> responseEntity = this.restTemplate.exchange(roomTypesUrl + "/1", HttpMethod.GET, requestEntity, RoomTypeDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(roomTypesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenStaffUser() {
    // given
    RoomTypeCreateDto createDto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    // when
    HttpEntity<RoomTypeCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<RoomTypeDto> responseEntity = this.restTemplate.exchange(roomTypesUrl, HttpMethod.POST, requestEntity, RoomTypeDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<RoomTypeDto> responseEntity = this.restTemplate.exchange(roomTypesUrl + "/1", HttpMethod.GET, requestEntity, RoomTypeDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(roomTypesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenClientUser() {
    // given
    RoomTypeCreateDto createDto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    // when
    HttpEntity<RoomTypeCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<RoomTypeDto> responseEntity = this.restTemplate.exchange(roomTypesUrl, HttpMethod.POST, requestEntity, RoomTypeDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<RoomTypeDto> responseEntity = this.restTemplate.exchange(roomTypesUrl + "/1", HttpMethod.GET, requestEntity, RoomTypeDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(roomTypesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }
}
