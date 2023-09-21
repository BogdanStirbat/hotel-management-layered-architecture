package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.ExceptionWrapperUtils.wrapWithInvalidDataException;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.BookingMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.BookingCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.BookingRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.BookingService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.RoomTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class BookingServiceImpl implements BookingService {

  private final RoomTypeService roomTypeService;
  private final BookingRepository bookingRepository;

  @Autowired
  public BookingServiceImpl(RoomTypeService roomTypeService, BookingRepository bookingRepository) {
    this.roomTypeService = roomTypeService;
    this.bookingRepository = bookingRepository;
  }

  @Override
  public Booking create(@NotNull @Valid BookingCreateDto createDto, @NotNull User user) {
    Booking booking = BookingMapper.INSTANCE.toEntity(createDto);
    booking.setUser(user);
    booking.setRoomType(wrapWithInvalidDataException(() -> roomTypeService.getById(createDto.getRoomTypeId())));

    return bookingRepository.save(booking);
  }

  @Override
  public Booking getById(@NotNull Long id) {

    return bookingRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find booking with id %s", id)));
  }
}
