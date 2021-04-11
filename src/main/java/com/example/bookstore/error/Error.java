package com.example.bookstore.error;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Error {
  String code;
  String message;
}
