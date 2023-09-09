package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.COUNTRIES;
import static com.bstirbat.hotelmanagement.layeredarchitecture.utils.HttpEntityUtil.createHttpEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.PageJacksonModule;
import com.bstirbat.hotelmanagement.layeredarchitecture.helper.AuthenticationHelper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CountryDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ConstraintValidationErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
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

class CountryControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CountryService countryService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value(value="${local.server.port}")
  private int port;

  private String countriesUrl;
  private String adminAuthToken;

  @BeforeEach
  void init() {
    objectMapper.registerModule(new PageJacksonModule());

    String url = "http://localhost:" + port;
    countriesUrl = url + COUNTRIES;
    AuthenticationHelper authenticationHelper = new AuthenticationHelper(restTemplate, url);
    adminAuthToken = authenticationHelper.obtainAdminToken();
  }

  @Test
  void createCountry() {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    HttpEntity<CountryCreateDto> requestEntity = createHttpEntity(createDto, adminAuthToken, APPLICATION_JSON);
    ResponseEntity<CountryDto> responseEntity = this.restTemplate.exchange(countriesUrl, HttpMethod.POST, requestEntity, CountryDto.class);

    assertEquals(CREATED, responseEntity.getStatusCode());
    assertTrue(responseEntity.getHeaders().get(HttpHeaders.LOCATION).get(0).startsWith(COUNTRIES));

    CountryDto responseDto = responseEntity.getBody();
    assertNotNull(responseDto);
    assertNotNull(responseDto.getId());
    assertEquals(createDto.getName(), responseDto.getName());
    assertEquals(createDto.getCountryCode(), responseDto.getCountryCode());
  }

  @Test
  void createCountry_whenInvalidDto() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);
    headers.setBearerAuth(adminAuthToken);

    HttpEntity<CountryCreateDto> requestEntity = createHttpEntity(new CountryCreateDto(), adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ConstraintValidationErrorDto> responseEntity = this.restTemplate.exchange(countriesUrl, HttpMethod.POST, requestEntity, ConstraintValidationErrorDto.class);

    assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

    ConstraintValidationErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(BAD_REQUEST.value(), errorDto.statusCode());
    assertEquals(2, errorDto.violationErrors().size());
    assertTrue(errorDto.message().contains("name"));
    assertTrue(errorDto.message().contains("countryCode"));
  }

  @Test
  void getById() {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    Country country = countryService.create(createDto);

    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<CountryDto> responseEntity = this.restTemplate.exchange(countriesUrl+ "/" + country.getId(), HttpMethod.GET, requestEntity, CountryDto.class);

    assertEquals(OK, responseEntity.getStatusCode());

    CountryDto responseDto = responseEntity.getBody();

    assertNotNull(responseDto);
    assertEquals(country.getId(), responseDto.getId());
    assertEquals(country.getName(), responseDto.getName());
    assertEquals(country.getCountryCode(), responseDto.getCountryCode());
  }

  @Test
  void getById_whenInvalidId() {
    long invalidId = 1L;

    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<ErrorDto> responseEntity = this.restTemplate.exchange(countriesUrl+ "/" + invalidId, HttpMethod.GET, requestEntity, ErrorDto.class);

    assertEquals(NOT_FOUND, responseEntity.getStatusCode());

    ErrorDto errorDto = responseEntity.getBody();
    assertNotNull(errorDto);
    assertEquals(NOT_FOUND.value(), errorDto.statusCode());
  }

  @Test
  void findAll() throws Exception {
    CountryCreateDto createDto1 = new CountryCreateDto();
    createDto1.setName("Australia");
    createDto1.setCountryCode("AU");

    CountryCreateDto createDto2 = new CountryCreateDto();
    createDto2.setName("Belgium");
    createDto2.setCountryCode("BE");

    CountryCreateDto createDto3 = new CountryCreateDto();
    createDto3.setName("Canada");
    createDto3.setCountryCode("CA");

    Country country1 = countryService.create(createDto1);
    Country country2 = countryService.create(createDto2);
    Country country3 = countryService.create(createDto3);

    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(countriesUrl)
        .queryParam("page", 0)
        .queryParam("size", 2);

    ResponseEntity<String> firstResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    builder = UriComponentsBuilder.fromHttpUrl(countriesUrl)
        .queryParam("page", 1)
        .queryParam("size", 2);

    ResponseEntity<String> secondResponseEntity = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, String.class);

    assertEquals(OK, firstResponseEntity.getStatusCode());

    Page<CountryDto> firstResponseList = objectMapper.readValue(firstResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, firstResponseList.getTotalElements());
    assertEquals(2, firstResponseList.getNumberOfElements());
    assertEquals(country1.getId(), firstResponseList.getContent().get(0).getId());
    assertEquals(country2.getId(), firstResponseList.getContent().get(1).getId());

    assertEquals(OK, secondResponseEntity.getStatusCode());

    Page<CountryDto> secondResponseList = objectMapper.readValue(secondResponseEntity.getBody(), new TypeReference<>() {});
    assertEquals(3, secondResponseList.getTotalElements());
    assertEquals(1, secondResponseList.getNumberOfElements());
    assertEquals(country3.getId(), secondResponseList.getContent().get(0).getId());
  }

  @Test
  void findAll_whenNoCountryExists() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);
    headers.setBearerAuth(adminAuthToken);

    HttpEntity<String> requestEntity = createHttpEntity("", adminAuthToken, APPLICATION_JSON);
    ResponseEntity<String> responseEntity = this.restTemplate.exchange(countriesUrl, HttpMethod.GET, requestEntity, String.class);

    assertEquals(OK, responseEntity.getStatusCode());

    Page<CountryDto> responseList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
    assertEquals(0, responseList.getTotalElements());
  }
}
