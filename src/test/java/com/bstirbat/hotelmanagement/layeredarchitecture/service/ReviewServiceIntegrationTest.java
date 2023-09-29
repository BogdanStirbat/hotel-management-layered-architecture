package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.helper.AddressHelper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.BookingCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.ReviewGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class ReviewServiceIntegrationTest extends AbstractIntegrationTest {

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

  private Booking booking;
  private User user;

  @BeforeEach
  public void setUp() {
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
    Review review = reviewService.create(createDto, user);

    // then
    assertNotNull(review.getId());
    assertEquals(createDto.getBookingId(), review.getBooking().getId());
    assertEquals(user.getId(), review.getUser().getId());
    assertNotNull(review.getReviewDate());
    assertEquals(createDto.getTitle(), review.getTitle());
    assertEquals(createDto.getBody(), review.getBody());
    assertEquals(createDto.getRating(), review.getRating());
  }

  @Test
  void create_whenInvalidBookingId() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(-1L)
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    // when & then
    assertThrows(InvalidDataException.class,
        () -> reviewService.create(createDto, user));
  }

  @Test
  void create_whenInvalidDto() {

    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> reviewService.create(new ReviewCreateDto(), user));

    // then
    assertTrue(ex.getMessage().contains("bookingId"));
    assertTrue(ex.getMessage().contains("rating"));
  }

  @Test
  void create_whenNullDto() {

    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> reviewService.create(null, user));
  }

  @Test
  void create_whenNullUser() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> reviewService.create(createDto, null));
  }

  @Test
  void getByd() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    Review review = reviewService.create(createDto, user);

    // when
    Review retrievedReview = reviewService.getById(review.getId());

    // then
    assertEquals(review.getId(), retrievedReview.getId());
    assertEquals(review.getBooking().getId(), retrievedReview.getBooking().getId());
    assertEquals(review.getUser().getId(), retrievedReview.getUser().getId());
    assertEquals(review.getReviewDate(), retrievedReview.getReviewDate());
    assertEquals(review.getTitle(), retrievedReview.getTitle());
    assertEquals(review.getBody(), retrievedReview.getBody());
    assertEquals(review.getRating(), retrievedReview.getRating());
  }

  @Test
  void getByd_whenNoReviewFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> reviewService.getById(-1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> reviewService.getById(null));
  }

  @Test
  void findAll() {
    // given
    ReviewCreateDto createDto = ReviewGenerator.ReviewCreateDtoBuilder.builder()
        .withBookingId(booking.getId())
        .withTitle("Exceptional")
        .withBody("The hotel is beautiful. Room is spacious, also the bathroom. Breakfast was amazing.")
        .withRating(10)
        .build();

    Review review = reviewService.create(createDto, user);

    // when
    Page<Review> reviews = reviewService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  reviews.getTotalElements());

    Review foundReview = reviews.getContent().get(0);
    assertEquals(review.getId(), foundReview.getId());
    assertEquals(review.getBooking().getId(), foundReview.getBooking().getId());
    assertEquals(review.getUser().getId(), foundReview.getUser().getId());
    assertEquals(review.getTitle(), foundReview.getTitle());
    assertEquals(review.getReviewDate(), foundReview.getReviewDate());
    assertEquals(review.getBody(), foundReview.getBody());
    assertEquals(review.getRating(), foundReview.getRating());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> reviewService.findAll(null));
  }
}
