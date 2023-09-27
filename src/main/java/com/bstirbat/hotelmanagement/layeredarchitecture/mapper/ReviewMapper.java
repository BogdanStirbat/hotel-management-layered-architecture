package com.bstirbat.hotelmanagement.layeredarchitecture.mapper;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ReviewDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewMapper {

  ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "booking", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "reviewDate", ignore = true)
  Review toEntity(ReviewCreateDto dto);

  @Mapping(target = "bookingId", source = "booking.id")
  @Mapping(target = "userId", source = "user.id")
  ReviewDto toDto(Review entity);
}
