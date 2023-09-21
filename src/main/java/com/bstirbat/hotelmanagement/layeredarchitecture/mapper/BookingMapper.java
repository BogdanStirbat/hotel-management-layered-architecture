package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.BookingCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.BookingDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {

  BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "roomType", ignore = true)
  @Mapping(target = "user", ignore = true)
  Booking toEntity(BookingCreateDto dto);

  @Mapping(target = "roomTypeId", source = "roomType.id")
  @Mapping(target = "userId", source = "user.id")
  BookingDto toDto(Booking entity);
}
