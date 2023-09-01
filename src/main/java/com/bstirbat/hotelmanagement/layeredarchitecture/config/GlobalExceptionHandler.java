package com.bstirbat.hotelmanagement.layeredarchitecture.config;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ConstraintValidationErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ErrorDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error.ViolationErrorDto;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorDto onResourceNotFoundException(ResourceNotFoundException ex) {

    return new ErrorDto(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ConstraintValidationErrorDto onConstraintViolationException(ConstraintViolationException ex) {
    List<ViolationErrorDto> violationErrors = ex.getConstraintViolations().stream()
        .map(violation -> new ViolationErrorDto(violation.getPropertyPath().toString(), violation.getMessage()))
        .toList();

    return new ConstraintValidationErrorDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), violationErrors);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ConstraintValidationErrorDto onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<ViolationErrorDto> violationErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> new ViolationErrorDto(fieldError.getField(), fieldError.getDefaultMessage()))
        .toList();

    return new ConstraintValidationErrorDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), violationErrors);
  }
}
