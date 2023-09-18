package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.RoomTypeCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.RoomTypeDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomTypeMapper {

  RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "hotel", ignore = true)
  RoomType toEntity(RoomTypeCreateDto dto);

  @Mapping(target = "hotelId", source = "hotel.id")
  RoomTypeDto toDto(RoomType entity);
}
