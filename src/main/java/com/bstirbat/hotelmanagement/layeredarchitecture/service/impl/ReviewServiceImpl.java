package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.ExceptionWrapperUtils.wrapWithInvalidDataException;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.ReviewMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.ReviewRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.BookingService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ReviewServiceImpl implements ReviewService {

  private final BookingService bookingService;
  private final ReviewRepository reviewRepository;

  @Autowired
  public ReviewServiceImpl(BookingService bookingService, ReviewRepository reviewRepository) {
    this.bookingService = bookingService;
    this.reviewRepository = reviewRepository;
  }

  @Override
  public Review create(@NotNull @Valid ReviewCreateDto createDto, @NotNull User user) {
    Review review = ReviewMapper.INSTANCE.toEntity(createDto);
    review.setUser(user);
    review.setBooking(wrapWithInvalidDataException(() -> bookingService.getById(createDto.getBookingId())));
    review.setReviewDate(LocalDate.now());

    return reviewRepository.save(review);
  }

  @Override
  public Review getById(@NotNull Long id) {

    return reviewRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find review with id %s", id)));
  }
}
