package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.REVIEWS;
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
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ReviewDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ConstraintValidationErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.BookingService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.HotelService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.ReviewService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.RoomTypeService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.UserService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ReviewGenerator;
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

class ReviewControllerIntegrationTest extends AbstractIntegrationTest {

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
  private ReviewService reviewService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value(value="${local.server.port}")
  private int port;

  private AuthenticationHelper authenticationHelper;

  private String reviewsUrl;
  private String adminAuthToken;

  private Booking booking;
  private User user;

  @BeforeEach
  void init() {
    objectMapper.registerModule(new PageJacksonModule());

    String url = "http://localhost:" + port;
    reviewsUrl = url + REVIEWS;
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
    RoomType roomType = roomTypeService.create(roomTypeCreateDto);

    user = userService.getByEmail("admin@test.com");

    BookingCreateDto bookingCreateDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();
    booking = bookingService.create(bookingCreateDto, user);
  }

  @Test
  void create() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    // when
    HttpEntity<ReviewCreateDto> requestEntity = createHttpEntity(createDto, adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ReviewDto> responseEntity = this.restTemplate.exchange(reviewsUrl, HttpMethod.POST, requestEntity, ReviewDto.class);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());
    assertTrue(responseEntity.getHeaders().get(HttpHeaders.LOCATION).get(0).startsWith(REVIEWS));

    ReviewDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertNotNull(responseDto.getId());
    assertEquals(createDto.getBookingId(), responseDto.getBookingId());
    assertEquals(user.getId(), responseDto.getUserId());
    assertNotNull(responseDto.getReviewDate());
    assertEquals(createDto.getTitle(), responseDto.getTitle());
    assertEquals(createDto.getBody(), responseDto.getBody());
    assertEquals(createDto.getRating(), responseDto.getRating());
  }

  @Test
  void create_whenInvalidDto() {
    // when
    HttpEntity<ReviewCreateDto> requestEntity = createHttpEntity(new ReviewCreateDto(), adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ConstraintValidationErrorDto> responseEntity = this.restTemplate.exchange(reviewsUrl, HttpMethod.POST, requestEntity, ConstraintValidationErrorDto.class);

    // then
    assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

    ConstraintValidationErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(BAD_REQUEST.value(), errorDto.statusCode());
    assertEquals(2, errorDto.violationErrors().size());
    assertTrue(errorDto.message().contains("bookingId"));
    assertTrue(errorDto.message().contains("rating"));
  }

  @Test
  void getById() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    Review review = reviewService.create(createDto, user);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ReviewDto> responseEntity = this.restTemplate.exchange(reviewsUrl + "/" + review.getId(), HttpMethod.GET, requestEntity, ReviewDto.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    ReviewDto responseDto = responseEntity.getBody();

    assertNotNull(responseDto);
    assertEquals(review.getId(), responseDto.getId());
    assertEquals(review.getBooking().getId(), responseDto.getBookingId());
    assertEquals(review.getUser().getId(), responseDto.getUserId());
    assertEquals(review.getReviewDate(), responseDto.getReviewDate());
    assertEquals(review.getTitle(), responseDto.getTitle());
    assertEquals(review.getBody(), responseDto.getBody());
    assertEquals(review.getRating(), responseDto.getRating());
  }

  @Test
  void getById_whenInvalidId() {
    // given
    long invalidId = 1L;

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ErrorDto> responseEntity = this.restTemplate.exchange(reviewsUrl + "/" + invalidId, HttpMethod.GET, requestEntity, ErrorDto.class);

    // then
    assertEquals(NOT_FOUND, responseEntity.getStatusCode());

    ErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(NOT_FOUND.value(), errorDto.statusCode());
  }

  @Test
  void findAll() throws Exception {
    // given
    ReviewCreateDto createDto1 = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    ReviewCreateDto createDto2 = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    ReviewCreateDto createDto3 = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    Review review1 = reviewService.create(createDto1, user);
    Review review2 = reviewService.create(createDto2, user);
    Review review3 = reviewService.create(createDto3, user);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(reviewsUrl)
        .queryParam("page", 0)
        .queryParam("size", 2);

    ResponseEntity<String> firstResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    builder = UriComponentsBuilder.fromHttpUrl(reviewsUrl)
        .queryParam("page", 1)
        .queryParam("size", 2);

    ResponseEntity<String> secondResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, firstResponseEntity.getStatusCode());

    Page<ReviewDto> firstResponseList = objectMapper.readValue(firstResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, firstResponseList.getTotalElements());
    assertEquals(2, firstResponseList.getNumberOfElements());
    assertEquals(review1.getId(), firstResponseList.getContent().get(0).getId());
    assertEquals(review2.getId(), firstResponseList.getContent().get(1).getId());

    assertEquals(OK, secondResponseEntity.getStatusCode());

    Page<ReviewDto> secondResponseList = objectMapper.readValue(secondResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, secondResponseList.getTotalElements());
    assertEquals(1, secondResponseList.getNumberOfElements());
    assertEquals(review3.getId(), secondResponseList.getContent().get(0).getId());
  }

  @Test
  void findAll_whenNoReviewExists() throws Exception {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(reviewsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    Page<AddressDto> responseList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
    assertEquals(0, responseList.getTotalElements());
  }

  @Test
  void create_whenAnonymousUser() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    // when
    HttpEntity<ReviewCreateDto> requestEntity = createHttpEntity(createDto, null, APPLICATION_JSON);
    ResponseEntity<ReviewDto> responseEntity = this.restTemplate.exchange(reviewsUrl, HttpMethod.POST, requestEntity, ReviewDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<ReviewDto> responseEntity = this.restTemplate.exchange(reviewsUrl + "/1", HttpMethod.GET, requestEntity, ReviewDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(reviewsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenStaffUser() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    // when
    HttpEntity<ReviewCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<ReviewDto> responseEntity = this.restTemplate.exchange(reviewsUrl, HttpMethod.POST, requestEntity, ReviewDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<ReviewDto> responseEntity = this.restTemplate.exchange(reviewsUrl + "/1", HttpMethod.GET, requestEntity, ReviewDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(reviewsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenClientUser() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    // when
    HttpEntity<ReviewCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<ReviewDto> responseEntity = this.restTemplate.exchange(reviewsUrl, HttpMethod.POST, requestEntity, ReviewDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<ReviewDto> responseEntity = this.restTemplate.exchange(reviewsUrl + "/1", HttpMethod.GET, requestEntity, ReviewDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(reviewsUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }
}
