package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ImageReferenceCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ImageReferenceDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.ImageReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImageReferenceMapper {

  ImageReferenceMapper INSTANCE = Mappers.getMapper(ImageReferenceMapper.class);

  @Mapping(target = "id", ignore = true)
  ImageReference toEntity(ImageReferenceCreateDto dto);

  ImageReferenceDto toDto(ImageReference entity);
}
