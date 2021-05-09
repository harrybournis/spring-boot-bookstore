package com.example.bookstore.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@AllArgsConstructor
public class ErrorResponseDto {
  Instant timestamp;
  @JsonProperty("errors")
  List<ErrorDto> errorDtos;
  String stacktrace;
}
