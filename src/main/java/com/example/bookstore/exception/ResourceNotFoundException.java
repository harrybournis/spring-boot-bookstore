package com.example.bookstore.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
  public ResourceNotFoundException(String errorCode, Object[] params) {
    super(errorCode, HttpStatus.NOT_FOUND, params);
  }
}
