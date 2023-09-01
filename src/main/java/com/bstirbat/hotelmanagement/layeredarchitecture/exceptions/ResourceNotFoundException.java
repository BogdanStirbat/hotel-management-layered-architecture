package com.bstirbat.hotelmanagement.layeredarchitecture.exceptions;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
