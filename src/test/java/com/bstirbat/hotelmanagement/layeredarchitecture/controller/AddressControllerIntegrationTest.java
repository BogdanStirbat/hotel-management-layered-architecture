package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.ADDRESSES;
import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.HttpEntityUtil.createHttpEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.PageJacksonModule;
import com.bstirbat.hotelmanagement.layeredarchitecture.helper.AuthenticationHelper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AddressCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CityCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.StreetCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AddressDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ConstraintValidationErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.City;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.AddressService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CityService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.StreetService;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.AddressGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CityGenerator.CityCreateDtoBuilder;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.CountryGenerator;
import com.bstirbat.hotelmanagement.layeredarchitecture.valuegenerator.StreetGenerator.StreetCreateDtoBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

class AddressControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CountryService countryService;

  @Autowired
  private CityService cityService;

  @Autowired
  private StreetService streetService;

  @Autowired
  private AddressService addressService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value(value="${local.server.port}")
  private int port;

  private AuthenticationHelper authenticationHelper;

  private String addressesUrl;
  private String adminAuthToken;

  private Street street;

  @BeforeEach
  void init() {
    objectMapper.registerModule(new PageJacksonModule());

    String url = "http://localhost:" + port;
    addressesUrl = url + ADDRESSES;
    authenticationHelper = new AuthenticationHelper(restTemplate, url);
    adminAuthToken = authenticationHelper.obtainAdminToken();

    CountryCreateDto countryCreateDto = CountryGenerator.CountryCreateDtoBuilder.builder()
        .withName("Germany")
        .withCountryCode("DE")
        .build();
    Country country = countryService.create(countryCreateDto);

    CityCreateDto cityCreateDto = CityCreateDtoBuilder.builder()
        .withCountryId(country.getId())
        .withName("Berlin")
        .build();
    City city = cityService.create(cityCreateDto);

    StreetCreateDto streetCreateDto = StreetCreateDtoBuilder.builder()
        .withCityId(city.getId())
        .withName("Street")
        .build();
    street = streetService.create(streetCreateDto);
  }

  @Test
  void create() {
    // given
    AddressCreateDto createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    // when
    HttpEntity<AddressCreateDto> requestEntity = createHttpEntity(createDto, adminAuthToken, APPLICATION_JSON);
    ResponseEntity<AddressDto> responseEntity = this.restTemplate.exchange(addressesUrl, HttpMethod.POST, requestEntity, AddressDto.class);

    // then
    assertEquals(CREATED, responseEntity.getStatusCode());
    assertTrue(responseEntity.getHeaders().get(HttpHeaders.LOCATION).get(0).startsWith(ADDRESSES));

    AddressDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertNotNull(responseDto.getId());
    assertEquals(createDto.getStreetId(), responseDto.getStreetId());
    assertEquals(createDto.getHouseNumber(), responseDto.getHouseNumber());
    assertEquals(createDto.getPostalCode(), responseDto.getPostalCode());
  }

  @Test
  void create_whenInvalidDto() {
    // when
    HttpEntity<AddressCreateDto> requestEntity = createHttpEntity(new AddressCreateDto(), adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ConstraintValidationErrorDto> responseEntity = this.restTemplate.exchange(addressesUrl, HttpMethod.POST, requestEntity, ConstraintValidationErrorDto.class);

    // then
    assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

    ConstraintValidationErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(BAD_REQUEST.value(), errorDto.statusCode());
    assertEquals(3, errorDto.violationErrors().size());
    assertTrue(errorDto.message().contains("houseNumber"));
    assertTrue(errorDto.message().contains("postalCode"));
    assertTrue(errorDto.message().contains("streetId"));
  }

  @Test
  void getById() {
    // given
    AddressCreateDto createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    Address address = addressService.create(createDto);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<AddressDto> responseEntity = this.restTemplate.exchange(addressesUrl + "/" + address.getId(), HttpMethod.GET, requestEntity, AddressDto.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    AddressDto responseDto = responseEntity.getBody();

    assertNotNull(responseDto);
    assertEquals(address.getId(), responseDto.getId());
    assertEquals(address.getHouseNumber(), responseDto.getHouseNumber());
    assertEquals(address.getPostalCode(), responseDto.getPostalCode());
    assertEquals(address.getStreet().getId(), responseDto.getStreetId());
  }

  @Test
  void getById_whenInvalidId() {
    // given
    long invalidId = 1L;

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ErrorDto> responseEntity = this.restTemplate.exchange(addressesUrl + "/" + invalidId, HttpMethod.GET, requestEntity, ErrorDto.class);

    // then
    assertEquals(NOT_FOUND, responseEntity.getStatusCode());

    ErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(NOT_FOUND.value(), errorDto.statusCode());
  }

  @Test
  void findAll() throws Exception {
    // given
    AddressCreateDto createDto1 = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    AddressCreateDto createDto2 = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    AddressCreateDto createDto3 = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    Address address1 = addressService.create(createDto1);
    Address address2 = addressService.create(createDto2);
    Address address3 = addressService.create(createDto3);

    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(addressesUrl)
        .queryParam("page", 0)
        .queryParam("size", 2);

    ResponseEntity<String> firstResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    builder = UriComponentsBuilder.fromHttpUrl(addressesUrl)
        .queryParam("page", 1)
        .queryParam("size", 2);

    ResponseEntity<String> secondResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, firstResponseEntity.getStatusCode());

    Page<AddressDto> firstResponseList = objectMapper.readValue(firstResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, firstResponseList.getTotalElements());
    assertEquals(2, firstResponseList.getNumberOfElements());
    assertEquals(address1.getId(), firstResponseList.getContent().get(0).getId());
    assertEquals(address2.getId(), firstResponseList.getContent().get(1).getId());

    assertEquals(OK, secondResponseEntity.getStatusCode());

    Page<AddressDto> secondResponseList = objectMapper.readValue(secondResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, secondResponseList.getTotalElements());
    assertEquals(1, secondResponseList.getNumberOfElements());
    assertEquals(address3.getId(), secondResponseList.getContent().get(0).getId());
  }

  @Test
  void findAll_whenNoStreetExists() throws Exception {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(addressesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(OK, responseEntity.getStatusCode());

    Page<AddressDto> responseList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
    assertEquals(0, responseList.getTotalElements());
  }

  @Test
  void create_whenAnonymousUser() {
    // given
    AddressCreateDto createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    // when
    HttpEntity<AddressCreateDto> requestEntity = createHttpEntity(createDto, null, APPLICATION_JSON);
    ResponseEntity<AddressDto> responseEntity = this.restTemplate.exchange(addressesUrl, HttpMethod.POST, requestEntity, AddressDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<AddressDto> responseEntity = this.restTemplate.exchange(addressesUrl + "/1", HttpMethod.GET, requestEntity, AddressDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenAnonymousUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", null, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(addressesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenStaffUser() {
    // given
    AddressCreateDto createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    // when
    HttpEntity<AddressCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<AddressDto> responseEntity = this.restTemplate.exchange(addressesUrl, HttpMethod.POST, requestEntity, AddressDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<AddressDto> responseEntity = this.restTemplate.exchange(addressesUrl + "/1", HttpMethod.GET, requestEntity, AddressDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenStaffUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainStaffToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(addressesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void create_whenClientUser() {
    // given
    AddressCreateDto createDto = AddressGenerator.AddressCreateDtoBuilder.builder()
        .withStreetId(street.getId())
        .withHouseNumber("10")
        .withPostalCode("114225")
        .build();

    // when
    HttpEntity<AddressCreateDto> requestEntity = createHttpEntity(createDto, authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<AddressDto> responseEntity = this.restTemplate.exchange(addressesUrl, HttpMethod.POST, requestEntity, AddressDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void getById_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<AddressDto> responseEntity = this.restTemplate.exchange(addressesUrl + "/1", HttpMethod.GET, requestEntity, AddressDto.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }

  @Test
  void findAll_whenClientUser() {
    // when
    HttpEntity<String> requestEntity = createHttpEntity("", authenticationHelper.obtainClientToken(), APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(addressesUrl, HttpMethod.GET, requestEntity, String.class);

    // then
    assertEquals(FORBIDDEN, responseEntity.getStatusCode());
  }
}
