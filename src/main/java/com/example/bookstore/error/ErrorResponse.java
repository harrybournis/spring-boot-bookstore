package com.example.bookstore.error;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@AllArgsConstructor
public class ErrorResponse {
  Instant timestamp;
  List<Error> errors;
  String stacktrace;
}
