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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/{id}")
  public ResponseEntity<ReviewDto> getById(@PathVariable Long id) {
    Review review = reviewService.getById(id);

    ReviewDto dto = ReviewMapper.INSTANCE.toDto(review);

    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<ReviewDto>> findAll(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page<ReviewDto> bookingDtos = reviewService.findAll(pageable)
        .map(ReviewMapper.INSTANCE::toDto);

    return ResponseEntity.ok(bookingDtos);
  }
}
