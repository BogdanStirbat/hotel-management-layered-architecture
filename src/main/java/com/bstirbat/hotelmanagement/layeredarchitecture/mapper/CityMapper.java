package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CityDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CityMapper {

  CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "country", ignore = true)
  City toEntity(CityCreateDto dto);

  @Mapping(target = "countryId", source = "country.id")
  CityDto toDto(City entity);
}
