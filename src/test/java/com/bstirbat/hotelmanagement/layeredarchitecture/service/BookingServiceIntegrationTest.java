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
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class BookingServiceIntegrationTest  extends AbstractIntegrationTest {

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

  private RoomType roomType;

  @BeforeEach
  public void setUp() {
    AddressHelper addressHelper = new AddressHelper(countryService, cityService, streetService, addressService);
    Address address = addressHelper.createAnAddress();

    HotelCreateDto createDto = HotelGenerator.HotelCreateDtoBuilder.builder()
        .withAddressId(address.getId())
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();
    Hotel hotel = hotelService.create(createDto);

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
    Booking booking = bookingService.create(createDto, user);

    // then
    assertNotNull(booking.getId());
    assertEquals(createDto.getRoomTypeId(), booking.getRoomType().getId());
    assertEquals(user.getId(), booking.getUser().getId());
    assertEquals(createDto.getCheckInDate(), booking.getCheckInDate());
    assertEquals(createDto.getCheckOutDate(), booking.getCheckOutDate());
  }

  @Test
  void create_whenInvalidRoomTypeId() {
    // given
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(-1L)
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    User user = userService.getByEmail("admin@test.com");

    // when & then
    assertThrows(InvalidDataException.class,
        () -> bookingService.create(createDto, user));
  }

  @Test
  void create_whenInvalidDto() {
    // given
    User user = userService.getByEmail("admin@test.com");

    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> bookingService.create(new BookingCreateDto(), user));

    // then
    assertTrue(ex.getMessage().contains("roomTypeId"));
    assertTrue(ex.getMessage().contains("checkInDate"));
    assertTrue(ex.getMessage().contains("checkOutDate"));
  }

  @Test
  void create_whenNullDto() {
    // given
    User user = userService.getByEmail("admin@test.com");

    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> bookingService.create(null, user));
  }

  @Test
  void create_whenNullUser() {
    // given
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> bookingService.create(createDto, null));
  }

  @Test
  void getByd() {
    // given
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    User user = userService.getByEmail("admin@test.com");

    Booking booking = bookingService.create(createDto, user);

    // when
    Booking retrievedBooking = bookingService.getById(booking.getId());

    // then
    assertEquals(booking.getId(), retrievedBooking.getId());
    assertEquals(booking.getRoomType().getId(), retrievedBooking.getRoomType().getId());
    assertEquals(booking.getUser().getId(), retrievedBooking.getUser().getId());
    assertEquals(booking.getCheckInDate(), retrievedBooking.getCheckInDate());
    assertEquals(booking.getCheckOutDate(), retrievedBooking.getCheckOutDate());
  }

  @Test
  void getByd_whenNoBookingFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> bookingService.getById(-1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> bookingService.getById(null));
  }

  @Test
  void findAll() {
    // given
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    User user = userService.getByEmail("admin@test.com");

    Booking booking = bookingService.create(createDto, user);

    // when
    Page<Booking> bookings = bookingService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  bookings.getTotalElements());

    Booking foundBooking = bookings.getContent().get(0);
    assertEquals(booking.getId(), foundBooking.getId());
    assertEquals(booking.getRoomType().getId(), foundBooking.getRoomType().getId());
    assertEquals(booking.getUser().getId(), foundBooking.getUser().getId());
    assertEquals(booking.getCheckInDate(), foundBooking.getCheckInDate());
    assertEquals(booking.getCheckOutDate(), foundBooking.getCheckOutDate());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> bookingService.findAll(null));
  }
}
