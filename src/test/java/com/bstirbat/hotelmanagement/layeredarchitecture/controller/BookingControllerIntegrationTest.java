package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.BOOKINGS;
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
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.BookingCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.BookingDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ConstraintValidationErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.BookingService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.HotelService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.RoomTypeService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.UserService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.Month;
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

class BookingControllerIntegrationTest extends AbstractIntegrationTest {

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
  private UserService userService;

  @Autowired
  private BookingService bookingService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value(value="${local.server.port}")
  private int port;

  private AuthenticationHelper authenticationHelper;

  private String bookingsUrl;
  private String adminAuthToken;

  private RoomType roomType;

  @BeforeEach
  void init() {
    objectMapper.registerModule(new PageJacksonModule());

    String url = "http://localhost:" + port;
    bookingsUrl = url + BOOKINGS;
    authenticationHelper = new AuthenticationHelper(restTemplate, url);
    adminAuthToken = authenticationHelper.obtainAdminToken();

    AddressHelper addressHelper = new AddressHelper(countryService, cityService, streetService, addressService);
    Address address = addressHelper.createAnAddress();

    HotelCreateDto hotelCreateDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();
    Hotel hotel = hotelService.create(hotelCreateDto);

    RoomTypeCreateDto roomTypeCreateDto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();
    roomType = roomTypeService.create(roomTypeCreateDto);
  }

  @Test
  void create() {
    // given
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    User user = userService.getByEmail("admin@test.com");

    // when
    HttpEntity<BookingCreateDto> requestEntity = createHttpEntity(createDto, adminAuthToken, APPLICATION_JSON);
    ResponseEntity<BookingDto> responseEntity = this.restTemplate.exchange(bookingsUrl, HttpMethod.POST, requestEntity, BookingDto.class);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());
    assertTrue(responseEntity.getHeaders().get(HttpHeaders.LOCATION).get(0).startsWith(BOOKINGS));

    BookingDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertNotNull(responseDto.getId());
    assertEquals(createDto.getRoomTypeId(), responseDto.getRoomTypeId());
    assertEquals(user.getId(), responseDto.getUserId());
    assertEquals(createDto.getCheckInDate(), responseDto.getCheckInDate());
    assertEquals(createDto.getCheckOutDate(), responseDto.getCheckOutDate());
  }

  @Test
  void create_whenInvalidDto() {
    // when
    HttpEntity<BookingCreateDto> requestEntity = createHttpEntity(new BookingCreateDto(), adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ConstraintValidationErrorDto> responseEntity = this.restTemplate.exchange(bookingsUrl, HttpMethod.POST, requestEntity, ConstraintValidationErrorDto.class);

    // then
    assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

    ConstraintValidationErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(BAD_REQUEST.value(), errorDto.statusCode());
    assertEquals(3, errorDto.violationErrors().size());
    assertTrue(errorDto.message().contains("roomTypeId"));
    assertTrue(errorDto.message().contains("checkInDate"));
    assertTrue(errorDto.message().contains("checkOutDate"));
  }

  @Test
  void getById() {
    // given
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    User user = userService.getByEmail("admin@test.com");

    Booking booking = bookingService.create(createDto, user);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<BookingDto> responseEntity = this.restTemplate.exchange(bookingsUrl + "/" + booking.getId(), HttpMethod.GET, requestEntity, BookingDto.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    BookingDto responseDto = responseEntity.getBody();

    assertNotNull(responseDto);
    assertEquals(booking.getId(), responseDto.getId());
    assertEquals(booking.getRoomType().getId(), responseDto.getRoomTypeId());
    assertEquals(booking.getUser().getId(), responseDto.getUserId());
    assertEquals(booking.getCheckInDate(), responseDto.getCheckInDate());
    assertEquals(booking.getCheckOutDate(), responseDto.getCheckOutDate());
  }

  @Test
  void getById_whenInvalidId() {
    // given
    long invalidId = 1L;

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ErrorDto> responseEntity = this.restTemplate.exchange(bookingsUrl + "/" + invalidId, HttpMethod.GET, requestEntity, ErrorDto.class);

    // then
    assertEquals(NOT_FOUND, responseEntity.getStatusCode());

    ErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(NOT_FOUND.value(), errorDto.statusCode());
  }

  @Test
  void findAll() throws Exception {
    // given
    BookingCreateDto createDto1 = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    BookingCreateDto createDto2 = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 17))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 19))
        .build();

    BookingCreateDto createDto3 = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 20))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 22))
        .build();

    User user = userService.getByEmail("admin@test.com");

    Booking booking1 = bookingService.create(createDto1, user);
    Booking booking2 = bookingService.create(createDto2, user);
    Booking booking3 = bookingService.create(createDto3, user);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(bookingsUrl)
        .queryParam("page", 0)
        .queryParam("size", 2);

    ResponseEntity<String> firstResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    builder = UriComponentsBuilder.fromHttpUrl(bookingsUrl)
        .queryParam("page", 1)
        .queryParam("size", 2);

    ResponseEntity<String> secondResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, firstResponseEntity.getStatusCode());

    Page<BookingDto> firstResponseList = objectMapper.readValue(firstResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, firstResponseList.getTotalElements());
    assertEquals(2, firstResponseList.getNumberOfElements());
    assertEquals(booking1.getId(), firstResponseList.getContent().get(0).getId());
    assertEquals(booking2.getId(), firstResponseList.getContent().get(1).getId());

    assertEquals(OK, secondResponseEntity.getStatusCode());

    Page<BookingDto> secondResponseList = objectMapper.readValue(secondResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, secondResponseList.getTotalElements());
    assertEquals(1, secondResponseList.getNumberOfElements());
    assertEquals(booking3.getId(), secondResponseList.getContent().get(0).getId());
  }

  @Test
  void findAll_whenNoImageReferenceExists() throws Exception {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(bookingsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    Page<AddressDto> responseList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
    assertEquals(0, responseList.getTotalElements());
  }

  @Test
  void create_whenAnonymousUser() {
    // given
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    // when
    HttpEntity<BookingCreateDto> requestEntity = createHttpEntity(createDto, null, APPLICATION_JSON);
    ResponseEntity<BookingDto> responseEntity = this.restTemplate.exchange(bookingsUrl, HttpMethod.POST, requestEntity, BookingDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<BookingDto> responseEntity = this.restTemplate.exchange(bookingsUrl + "/1", HttpMethod.GET, requestEntity, BookingDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(bookingsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenStaffUser() {
    // given
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    // when
    HttpEntity<BookingCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<BookingDto> responseEntity = this.restTemplate.exchange(bookingsUrl, HttpMethod.POST, requestEntity, BookingDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<BookingDto> responseEntity = this.restTemplate.exchange(bookingsUrl + "/1", HttpMethod.GET, requestEntity, BookingDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(bookingsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenClientUser() {
    // given
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    // when
    HttpEntity<BookingCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<BookingDto> responseEntity = this.restTemplate.exchange(bookingsUrl, HttpMethod.POST, requestEntity, BookingDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<BookingDto> responseEntity = this.restTemplate.exchange(bookingsUrl + "/1", HttpMethod.GET, requestEntity, BookingDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(bookingsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }
}
