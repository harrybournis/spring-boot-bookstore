package com.example.bookstore.dto.error;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorDto {
  String code;
  String message;
}
