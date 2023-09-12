package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.StreetDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StreetMapper {

  StreetMapper INSTANCE = Mappers.getMapper(StreetMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "city", ignore = true)
  Street toEntity(StreetCreateDto dto);

  @Mapping(target = "cityId", source = "city.id")
  StreetDto toDto(Street entity);
}
