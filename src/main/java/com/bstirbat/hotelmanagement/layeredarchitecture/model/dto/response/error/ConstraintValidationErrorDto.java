package com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.response.error;

import java.util.List;

public record ConstraintValidationErrorDto(int statusCode, String message, List<ViolationErrorDto> violationErrors) {

}
