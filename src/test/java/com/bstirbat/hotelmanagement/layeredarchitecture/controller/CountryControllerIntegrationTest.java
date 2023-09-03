package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.COUNTRIES;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bstirbat.hotelmanagement.layeredarchitecture.AbstractIntegrationTest;
import com.bstirbat.hotelmanagement.layeredarchitecture.PageJacksonModule;
import com.bstirbat.hotelmanagement.layeredarchitecture.config.GlobalExceptionHandler;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.CountryDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class CountryControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CountryService countryService;

  @Autowired
  private CountryController countryController;

  @Autowired
  private GlobalExceptionHandler globalExceptionHandler;

  private MockMvc mockMvc;

  @BeforeEach
  void init() {
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(countryController)
            .setControllerAdvice(globalExceptionHandler)
            .build();

    objectMapper.registerModule(new PageJacksonModule());
  }

  @Test
  void createCountry_isSuccessful() throws Exception {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    String contentAsString = this.mockMvc
        .perform(post(COUNTRIES)
            .content(objectMapper.writeValueAsString(createDto))
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(header().string(HttpHeaders.LOCATION, startsWith(COUNTRIES)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    CountryDto responseDto = objectMapper.readValue(contentAsString, CountryDto.class);

    assertNotNull(responseDto.getId());
    assertEquals(createDto.getName(), responseDto.getName());
    assertEquals(createDto.getCountryCode(), responseDto.getCountryCode());
  }

  @Test
  void getById_isSuccessful() throws Exception {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    Country country = countryService.create(createDto);

    String contentAsString = this.mockMvc
        .perform(get(COUNTRIES + "/" + country.getId())
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andReturn()
        .getResponse()
        .getContentAsString();
    CountryDto responseDto = objectMapper.readValue(contentAsString, CountryDto.class);

    assertEquals(country.getId(), responseDto.getId());
    assertEquals(country.getName(), responseDto.getName());
    assertEquals(country.getCountryCode(), responseDto.getCountryCode());
  }

  @Test
  void findAll_isSuccessful() throws Exception {
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

    String firstContentAsString = this.mockMvc
        .perform(get(COUNTRIES)
            .queryParam("page", "0")
            .queryParam("size", "2")
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andReturn()
        .getResponse()
        .getContentAsString();
    Page<CountryDto> firstResponseList = objectMapper.readValue(firstContentAsString, new TypeReference<>() {});

    String secondContentAsString = this.mockMvc
        .perform(get(COUNTRIES)
            .queryParam("page", "1")
            .queryParam("size", "2")
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andReturn()
        .getResponse()
        .getContentAsString();
    Page<CountryDto> secondResponseList = objectMapper.readValue(secondContentAsString, new TypeReference<>() {});

    assertEquals(3, firstResponseList.getTotalElements());
    assertEquals(2, firstResponseList.getNumberOfElements());
    assertEquals(country1.getId(), firstResponseList.getContent().get(0).getId());
    assertEquals(country2.getId(), firstResponseList.getContent().get(1).getId());

    assertEquals(3, secondResponseList.getTotalElements());
    assertEquals(1, secondResponseList.getNumberOfElements());
    assertEquals(country3.getId(), secondResponseList.getContent().get(0).getId());
  }

}
