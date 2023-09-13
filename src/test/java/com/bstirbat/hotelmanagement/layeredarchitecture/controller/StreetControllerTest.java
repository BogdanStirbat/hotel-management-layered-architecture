package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.StreetDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator.StreetBuilder;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator.StreetCreateDtoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

class StreetControllerTest {

  private StreetService streetService;
  private StreetController streetController;

  private Street street;

  private StreetCreateDto createDto;

  @BeforeEach
  public void setUp() {
    streetService = mock(StreetService.class);
    streetController = new StreetController(streetService);

    City city = CityGenerator.CityBuilder.builder()
        .withId(2L)
        .withName("Berlin")
        .build();

    createDto = StreetCreateDtoBuilder.builder()
        .withCityId(city.getId())
        .withName("Street")
        .build();

    street = StreetBuilder.builder()
        .withId(3L)
        .withName("Street")
        .withCity(city)
        .build();
  }

  @Test
  void create() {
    // given
    when(streetService.create(any())).thenReturn(street);

    // when
    ResponseEntity<StreetDto> responseEntity = streetController.create(createDto);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());

    StreetDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(street.getId(), responseDto.getId());
    assertEquals(street.getCity().getId(), responseDto.getCityId());
    assertEquals(street.getName(), responseDto.getName());
  }

  @Test
  void getById() {
    // given
    when(streetService.getById(3L)).thenReturn(street);

    // when
    ResponseEntity<StreetDto> responseEntity = streetController.getById(3L);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    StreetDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertEquals(street.getId(), responseDto.getId());
    assertEquals(street.getCity().getId(), responseDto.getCityId());
    assertEquals(street.getName(), responseDto.getName());
  }

  @Test
  void findAll() {
    // when
    when(streetService.findAll(any())).thenReturn(Page.empty());

    // then
    ResponseEntity<Page<StreetDto>> responseEntity = streetController.findAll(0, 20);
    assertEquals(OK, responseEntity.getStatusCode());
  }
}
