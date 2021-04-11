package com.example.bookstore.error;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@AllArgsConstructor
public class ErrorResponse {
//  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  Instant timestamp;
  List<Error> errors;
  String stacktrace;
}
