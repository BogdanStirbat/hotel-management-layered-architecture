package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.enums.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.config.user.UserDetailsImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.BookingCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.BookingDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.BookingService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.BookingGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.UserGenerator.UserBuilder;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

class BookingControllerTest {

  private BookingService bookingService;

  private BookingController bookingController;

  private RoomType roomType;
  private User user;

  @BeforeEach
  public void setUp() {
    bookingService = mock(BookingService.class);
    bookingController = new BookingController(bookingService);

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
    BookingCreateDto createDto = BookingGenerator.BookingCreateDtoBuilder.builder()
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

    when(bookingService.create(any(), any())).thenReturn(booking);

    UserDetailsImpl userDetails = new UserDetailsImpl(user);
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);

    // when
    ResponseEntity<BookingDto> responseEntity = bookingController.create(createDto, authentication);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    BookingDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(booking.getId(), responseDto.getId());
    assertEquals(booking.getRoomType().getId(), responseDto.getRoomTypeId());
    assertEquals(booking.getUser().getId(), responseDto.getUserId());
    assertEquals(booking.getCheckInDate(), responseDto.getCheckInDate());
    assertEquals(booking.getCheckOutDate(), responseDto.getCheckOutDate());
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

    when(bookingService.getById(booking.getId())).thenReturn(booking);

    // when
    ResponseEntity<BookingDto> responseEntity = bookingController.getById(booking.getId());

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
  void findAll() {
    // when
    when(bookingService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<BookingDto>> responseEntity = bookingController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
