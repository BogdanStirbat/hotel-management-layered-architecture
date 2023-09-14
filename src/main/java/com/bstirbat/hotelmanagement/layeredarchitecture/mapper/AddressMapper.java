package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {

  AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "street", ignore = true)
  Address toEntity(AddressCreateDto dto);

  @Mapping(target = "streetId", source = "street.id")
  AddressDto toDto(Address entity);
}
