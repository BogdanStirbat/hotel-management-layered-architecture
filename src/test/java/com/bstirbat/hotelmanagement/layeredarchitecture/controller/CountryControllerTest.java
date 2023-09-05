package com.bstirbat.hotelmanagement.layeredarchitecture.controller;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.COUNTRIES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bstirbat.hotelmanagement.layeredarchitecture.config.JwtAuthenticationFilter;
import com.bstirbat.hotelmanagement.layeredarchitecture.config.SecurityConfiguration;
import com.bstirbat.hotelmanagement.layeredarchitecture.config.user.UserDetailsServiceImpl;
import com.bstirbat.hotelmanagement.layeredarchitecture.enums.Role;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.CountryCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.User;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.UserRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.CountryService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.JwtService;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import({JwtAuthenticationFilter.class, UserDetailsServiceImpl.class, UserServiceImpl.class, SecurityConfiguration.class})
@WebMvcTest(CountryController.class)
class CountryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CountryService countryService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private JwtService jwtService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setUp() {
    User user = new User();
    user.setRole(Role.ADMIN);
    user.setEmail("admin@test.com");
    user.setPassword("secret");

    when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
    when(jwtService.extractUserName(any())).thenReturn("admin@test.com");
    when(jwtService.isTokenValid(any(), any())).thenReturn(true);
  }

  @Test
  void createCountry() throws Exception {
    CountryCreateDto createDto = new CountryCreateDto();
    createDto.setName("Germany");
    createDto.setCountryCode("DE");

    Country country = new Country();
    country.setId(1L);
    country.setName("Germany");
    country.setCountryCode("DE");

    when(countryService.create(any())).thenReturn(country);

    this.mockMvc
        .perform(
            post(COUNTRIES)
                .header("Authorization", "Bearer test")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
        .andDo(print())
        .andExpect(status().isCreated());
  }
}
