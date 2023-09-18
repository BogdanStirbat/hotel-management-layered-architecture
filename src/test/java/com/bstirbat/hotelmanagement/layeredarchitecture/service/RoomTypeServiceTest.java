package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.RoomTypeRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.RoomTypeServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class RoomTypeServiceTest {

  private RoomTypeService roomTypeService;

  private HotelService hotelService;
  private RoomTypeRepository roomTypeRepository;

  private Hotel hotel;

  @BeforeEach
  public void setUp() {
    hotelService = mock(HotelService.class);
    roomTypeRepository = mock(RoomTypeRepository.class);

    roomTypeService = new RoomTypeServiceImpl(hotelService, roomTypeRepository);

    hotel = HotelGenerator.HotelBuilder.builder()
        .withId(5L)
        .withName("Marriott")
        .withDescription("Well set in Berlin, Marriott Hotel provides air-conditioned rooms, free WiFi.")
        .build();
  }

  @Test
  void create() {
    // given
    RoomTypeCreateDto dto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
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

    when(roomTypeRepository.save(any())).thenReturn(roomType);

    // when
    RoomType createdRoomType = roomTypeService.create(dto);

    // then
    assertEquals(roomType.getId(), createdRoomType.getId());
    assertEquals(roomType.getHotel().getId(), createdRoomType.getHotel().getId());
    assertEquals(roomType.getName(), createdRoomType.getName());
    assertEquals(roomType.getDescription(), createdRoomType.getDescription());
    assertEquals(roomType.getNumberOfAvailableRooms(), createdRoomType.getNumberOfAvailableRooms());
  }

  @Test
  void create_whenInvalidAddressId() {
    // given
    RoomTypeCreateDto dto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    when(hotelService.getById(hotel.getId())).thenThrow(new ResourceNotFoundException("Could not find a hotel with id 5"));

    // when & then
    assertThrows(InvalidDataException.class, () -> roomTypeService.create(dto));
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

    when(roomTypeRepository.findById(roomType.getId())).thenReturn(Optional.of(roomType));

    // when
    RoomType retrievedRoomType = roomTypeService.getById(roomType.getId());

    // then
    assertEquals(roomType.getId(), retrievedRoomType.getId());
    assertEquals(roomType.getHotel().getId(), retrievedRoomType.getHotel().getId());
    assertEquals(roomType.getName(), retrievedRoomType.getName());
    assertEquals(roomType.getDescription(), retrievedRoomType.getDescription());
    assertEquals(roomType.getNumberOfAvailableRooms(), retrievedRoomType.getNumberOfAvailableRooms());
  }

  @Test
  void findAll() {
    // given
    Page<RoomType> page = Page.empty();

    when(roomTypeRepository.findAll(any(Pageable.class))).thenReturn(page);

    // when
    Page<RoomType> roomTypes = roomTypeService.findAll(Pageable.unpaged());

    // then
    assertTrue(roomTypes.isEmpty());
  }
}
