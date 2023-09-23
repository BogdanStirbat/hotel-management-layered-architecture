package com.bstirbat.hotelmanagement.layeredarchitecture.config;

import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.ADDRESSES;
import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.BOOKINGS;
import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.CITIES;
import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.COUNTRIES;
import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.FACILITIES;
import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.HOTELS;
import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.IMAGE_REFERENCES;
import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.ROOM_TYPES;
import static com.bstirbat.hotelmanagement.layeredarchitecture.constants.Paths.STREETS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.bstirbat.hotelmanagement.layeredarchitecture.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final UserDetailsService userDetailsService;

  @Autowired
  public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request -> request
            .requestMatchers("/authentications/login/**").permitAll()
            .requestMatchers("/authentications/signup/**").permitAll()

            .requestMatchers(HttpMethod.GET, "/test").permitAll()
            .requestMatchers(HttpMethod.POST, "/test").hasRole(Role.STAFF.name())

            .requestMatchers(COUNTRIES + "/**").hasRole(Role.ADMIN.name())
            .requestMatchers(CITIES + "/**").hasRole(Role.ADMIN.name())
            .requestMatchers(STREETS + "/**").hasRole(Role.ADMIN.name())
            .requestMatchers(ADDRESSES + "/**").hasRole(Role.ADMIN.name())
            .requestMatchers(HOTELS + "/**").hasRole(Role.ADMIN.name())
            .requestMatchers(ROOM_TYPES + "/**").hasRole(Role.ADMIN.name())
            .requestMatchers(IMAGE_REFERENCES + "/**").hasRole(Role.ADMIN.name())
            .requestMatchers(FACILITIES + "/**").hasRole(Role.ADMIN.name())
            .requestMatchers(BOOKINGS + "/**").hasRole(Role.ADMIN.name())

            .anyRequest().authenticated())
        .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider()).addFilterBefore(
            jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
