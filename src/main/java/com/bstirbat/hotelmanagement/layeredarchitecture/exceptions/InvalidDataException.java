package com.bstirbat.hotelmanagement.layeredarchitecture.exceptions;

public class InvalidDataException extends RuntimeException {

  public InvalidDataException(String message) {
    super(message);
  }
}
