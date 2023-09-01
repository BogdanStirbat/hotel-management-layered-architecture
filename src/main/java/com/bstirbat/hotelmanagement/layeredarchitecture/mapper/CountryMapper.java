package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CountryDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CountryMapper {

  CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

  @Mapping(target = "id", ignore = true)
  Country toEntity(CountryCreateDto dto);

  CountryDto toDto(Country entity);
}
