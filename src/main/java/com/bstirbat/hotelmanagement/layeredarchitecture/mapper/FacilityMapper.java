package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.FacilityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.FacilityDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Facility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FacilityMapper {

  FacilityMapper INSTANCE = Mappers.getMapper(FacilityMapper.class);

  @Mapping(target = "id", ignore = true)
  Facility toEntity(FacilityCreateDto dto);

  FacilityDto toDto(Facility entity);
}
