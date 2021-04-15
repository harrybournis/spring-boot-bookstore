package com.example.bookstore.error.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
  public ResourceNotFoundException(String errorCode, Object[] params) {
    super(errorCode, HttpStatus.BAD_REQUEST, params);
  }
}
