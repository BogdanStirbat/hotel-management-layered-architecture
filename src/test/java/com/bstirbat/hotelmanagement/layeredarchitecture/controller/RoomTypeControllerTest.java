package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.RoomTypeDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.RoomTypeService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

class RoomTypeControllerTest {

  private RoomTypeService roomTypeService;

  private RoomTypeController roomTypeController;

  private Hotel hotel;

  @BeforeEach
  public void setUp() {
    roomTypeService = mock(RoomTypeService.class);
    roomTypeController = new RoomTypeController(roomTypeService);

    hotel = HotelGenerator.HotelBuilder.builder()
        .withId(5L)
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();
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

    RoomType roomType = RoomTypeGenerator.RoomTypeBuilder.builder()
        .withId(6L)
        .withHotel(hotel)
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    when(roomTypeService.create(any())).thenReturn(roomType);

    // when
    ResponseEntity<RoomTypeDto> responseEntity = roomTypeController.create(createDto);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    RoomTypeDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(roomType.getId(), responseDto.getId());
    assertEquals(roomType.getHotel().getId(), responseDto.getHotelId());
    assertEquals(roomType.getName(), responseDto.getName());
    assertEquals(roomType.getDescription(), responseDto.getDescription());
    assertEquals(roomType.getNumberOfAvailableRooms(), responseDto.getNumberOfAvailableRooms());
  }

  @Test
  void getById() {
    // given
    RoomType roomType = RoomTypeGenerator.RoomTypeBuilder.builder()
        .withId(6L)
        .withHotel(hotel)
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    when(roomTypeService.getById(roomType.getId())).thenReturn(roomType);

    // when
    ResponseEntity<RoomTypeDto> responseEntity = roomTypeController.getById(roomType.getId());

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
  void findAll() {
    // when
    when(roomTypeService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<RoomTypeDto>> responseEntity = roomTypeController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
