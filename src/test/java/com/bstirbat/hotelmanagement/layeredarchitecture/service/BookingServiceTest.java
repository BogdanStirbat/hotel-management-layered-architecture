package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static com.bstirbat.hotelmanagement.layeredarchitecture.enums.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.BookingCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.BookingRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.BookingServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.UserGenerator.UserBuilder;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class BookingServiceTest {

  private BookingService bookingService;

  private RoomTypeService roomTypeService;
  private BookingRepository bookingRepository;

  private RoomType roomType;
  private User user;

  @BeforeEach
  public void setUp() {
    roomTypeService = mock(RoomTypeService.class);
    bookingRepository = mock(BookingRepository.class);

    bookingService = new BookingServiceImpl(roomTypeService, bookingRepository);

    roomType = RoomTypeGenerator.RoomTypeBuilder.builder()
        .withId(8L)
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    user = UserBuilder.builder()
        .withId(2L)
        .withEmail("admin@test.com")
        .withRole(ADMIN)
        .build();
  }

  @Test
  void create() {
    // given
    BookingCreateDto dto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    Booking booking = BookingGenerator.BookingBuilder.builder()
        .withId(9L)
        .withRoomType(roomType)
        .withUser(user)
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    when(bookingRepository.save(any())).thenReturn(booking);

    // when
    Booking createdBooking = bookingService.create(dto, user);

    // then
    assertEquals(booking.getId(), createdBooking.getId());
    assertEquals(booking.getRoomType().getId(), createdBooking.getRoomType().getId());
    assertEquals(booking.getUser().getId(), createdBooking.getUser().getId());
    assertEquals(booking.getCheckInDate(), createdBooking.getCheckInDate());
    assertEquals(booking.getCheckOutDate(), createdBooking.getCheckOutDate());
  }

  @Test
  void create_whenInvalidRoomTypeId() {
    // given
    BookingCreateDto dto = BookingGenerator.BookingCreateDtoBuilder.builder()
        .withRoomTypeId(roomType.getId())
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    when(roomTypeService.getById(roomType.getId())).thenThrow(new ResourceNotFoundException("Could not find a room type with id 8"));

    // when & then
    assertThrows(InvalidDataException.class, () -> bookingService.create(dto, user));
  }

  @Test
  void getById() {
    // given
    Booking booking = BookingGenerator.BookingBuilder.builder()
        .withId(9L)
        .withRoomType(roomType)
        .withUser(user)
        .withCheckInDate(LocalDate.of(2023, Month.OCTOBER, 14))
        .withCheckOutDate(LocalDate.of(2023, Month.OCTOBER, 16))
        .build();

    when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

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
  void getById_whenNoBookingFound() {
    // given
    when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ResourceNotFoundException.class, () -> bookingService.getById(1L));
  }

  @Test
  void findAll() {
    // given
    Page<Booking> page = Page.empty();

    when(bookingRepository.findAll(any(Pageable.class))).thenReturn(page);

    // when
    Page<Booking> bookings = bookingService.findAll(Pageable.unpaged());

    // then
    assertTrue(bookings.isEmpty());
  }
}
