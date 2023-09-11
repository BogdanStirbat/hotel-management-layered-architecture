package com.bstirbat.hotelmanagement.layeredarchitecture.utils;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.InvalidDataException;
import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import java.util.function.Supplier;

public class ExceptionWrapperUtils {

  private ExceptionWrapperUtils() {

  }

  public static <T> T wrapWithInvalidDataException(Supplier<T> supplier) {
    try {
      return supplier.get();
    } catch (ResourceNotFoundException ex) {
      throw new InvalidDataException(ex.getMessage());
    }
  }
}
