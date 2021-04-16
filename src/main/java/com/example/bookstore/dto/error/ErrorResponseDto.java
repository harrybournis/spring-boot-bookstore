package com.example.bookstore.dto.error;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@AllArgsConstructor
public class ErrorResponseDto {
  Instant timestamp;
  List<ErrorDto> errorDtos;
  String stacktrace;
}
