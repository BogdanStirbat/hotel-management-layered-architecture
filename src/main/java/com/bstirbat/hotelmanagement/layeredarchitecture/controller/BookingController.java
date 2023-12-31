package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.BOOKINGS;
import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.UserUtils.extractUser;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.BookingMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.BookingCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.BookingDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Booking;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.BookingService;
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
@RequestMapping(BOOKINGS)
public class BookingController {

  private final BookingService bookingService;

  @Autowired
  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @PostMapping
  public ResponseEntity<BookingDto> create(@RequestBody BookingCreateDto createDto, Authentication authentication) {
    User user = extractUser(authentication);

    Booking booking = bookingService.create(createDto, user);

    BookingDto dto = BookingMapper.INSTANCE.toDto(booking);

    URI resourceLocation = fromPath(BOOKINGS)
        .pathSegment("{id}")
        .buildAndExpand(booking.getId())
        .toUri();

    return ResponseEntity.created(resourceLocation).body(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookingDto> getById(@PathVariable Long id) {
    Booking booking = bookingService.getById(id);

    BookingDto dto = BookingMapper.INSTANCE.toDto(booking);

    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<BookingDto>> findAll(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("checkInDate"));

    Page<BookingDto> bookingDtos = bookingService.findAll(pageable)
        .map(BookingMapper.INSTANCE::toDto);

    return ResponseEntity.ok(bookingDtos);
  }
}
