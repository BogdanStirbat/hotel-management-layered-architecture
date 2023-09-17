package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.HotelCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.HotelDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HotelMapper {

  HotelMapper INSTANCE = Mappers.getMapper(HotelMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "address", ignore = true)
  Hotel toEntity(HotelCreateDto dto);

  @Mapping(target = "addressId", source = "address.id")
  HotelDto toDto(Hotel entity);
}
