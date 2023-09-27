package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.REVIEWS;
import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.UserUtils.extractUser;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.ReviewMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ReviewCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.ReviewDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.ReviewService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(REVIEWS)
public class ReviewController {

  private final ReviewService reviewService;

  @Autowired
  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }
  
  @PostMapping
  public ResponseEntity<ReviewDto> create(@RequestBody ReviewCreateDto createDto, Authentication authentication) {
    User user = extractUser(authentication);

    Review review = reviewService.create(createDto, user);

    ReviewDto dto = ReviewMapper.INSTANCE.toDto(review);

    URI resourceLocation = fromPath(REVIEWS)
        .pathSegment("{id}")
        .buildAndExpand(review.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }
}
