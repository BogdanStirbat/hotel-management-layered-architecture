package com.bstirbat.hotelmanagement.layeredarchitecture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.helper.AddressHelper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.HotelGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.RoomTypeGenerator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class RoomTypeServiceIntegrationTest extends AbstractIntegrationTest {

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

  private Hotel hotel;

  @BeforeEach
  public void setUp() {
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
    RoomType roomType = roomTypeService.create(createDto);

    // then
    assertNotNull(roomType.getId());
    assertEquals(createDto.getHotelId(), roomType.getHotel().getId());
    assertEquals(createDto.getName(), roomType.getName());
    assertEquals(createDto.getDescription(), roomType.getDescription());
    assertEquals(createDto.getNumberOfAvailableRooms(), roomType.getNumberOfAvailableRooms());
  }

  @Test
  void create_whenInvalidAddressId() {
    // given
    RoomTypeCreateDto createDto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(-1L)
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    // when & then
    assertThrows(InvalidDataException.class,
        () -> roomTypeService.create(createDto));
  }

  @Test
  void create_whenInvalidDto() {
    // when
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
        () -> roomTypeService.create(new RoomTypeCreateDto()));

    // then
    assertTrue(ex.getMessage().contains("hotelId"));
    assertTrue(ex.getMessage().contains("name"));
    assertTrue(ex.getMessage().contains("description"));
    assertTrue(ex.getMessage().contains("numberOfAvailableRooms"));
  }

  @Test
  void create_whenNullDto() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> roomTypeService.create(null));
  }

  @Test
  void getByd() {
    // given
    RoomTypeCreateDto createDto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    RoomType roomType = roomTypeService.create(createDto);

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
  void getByd_whenNoRoomTypeFound() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> roomTypeService.getById(-1L));
  }

  @Test
  void getByd_whenNullId() {
    // when & then
    assertThrows(ConstraintViolationException.class, () -> roomTypeService.getById(null));
  }

  @Test
  void findAll() {
    // given
    RoomTypeCreateDto createDto = RoomTypeGenerator.RoomTypeCreateDtoBuilder.builder()
        .withHotelId(hotel.getId())
        .withName("King Guest Room")
        .withDescription("The elegant, quiet double room features twin beds or a double bed.")
        .withNumberOfAvailableRooms(4)
        .build();

    RoomType roomType = roomTypeService.create(createDto);

    // when
    Page<RoomType> roomTypes = roomTypeService.findAll(Pageable.unpaged());

    // then
    assertEquals(1,  roomTypes.getTotalElements());

    RoomType foundRoomType = roomTypes.getContent().get(0);
    assertEquals(roomType.getId(), foundRoomType.getId());
    assertEquals(roomType.getHotel().getId(), foundRoomType.getHotel().getId());
    assertEquals(roomType.getName(), foundRoomType.getName());
    assertEquals(roomType.getDescription(), foundRoomType.getDescription());
    assertEquals(roomType.getNumberOfAvailableRooms(), foundRoomType.getNumberOfAvailableRooms());
  }

  @Test
  void findAll_whenNullArgument() {
    // when & then
    assertThrows(ConstraintViolationException.class,
        () -> roomTypeService.findAll(null));
  }
}
