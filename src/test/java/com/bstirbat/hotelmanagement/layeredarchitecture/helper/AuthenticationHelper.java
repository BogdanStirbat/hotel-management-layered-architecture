package com.bstirbat.hotelmanagement.layeredarchitecture.helper;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.AuthenticationRequest;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.AuthenticationResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class AuthenticationHelper {

  private static final String ADMIN_USERNAME = "admin@test.com";
  private static final String ADMIN_PASSWORD = "admin";

  private static final String STAFF_USERNAME = "staff@test.com";
  private static final String STAFF_PASSWORD = "staff";

  private static final String CLIENT_USERNAME = "client@test.com";
  private static final String CLIENT_PASSWORD = "client";

  private final TestRestTemplate restTemplate;
  private final String url;

  public AuthenticationHelper(TestRestTemplate restTemplate, String url) {
    this.restTemplate = restTemplate;
    this.url = url;
  }

  public String obtainAdminToken() {

    return obtainAuthenticationToken(ADMIN_USERNAME, ADMIN_PASSWORD);
  }

  public String obtainStaffToken() {

    return obtainAuthenticationToken(STAFF_USERNAME, STAFF_PASSWORD);
  }

  public String obtainClientToken() {

    return obtainAuthenticationToken(CLIENT_USERNAME, CLIENT_PASSWORD);
  }

  public String obtainAuthenticationToken(String username, String password) {
    final String authUrl = url + "/authentications/login";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);

    AuthenticationRequest request = new AuthenticationRequest();
    request.setEmail(username);
    request.setPassword(password);

    HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(request, headers);

    AuthenticationResponse response = restTemplate.postForObject(authUrl, entity, AuthenticationResponse.class);

    return response.getToken();
  }
}
