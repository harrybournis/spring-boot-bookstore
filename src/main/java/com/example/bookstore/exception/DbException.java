package com.example.bookstore.exception;

import org.springframework.http.HttpStatus;

public class DbException extends Exception {
  public DbException(Exception e) {
    super(e);
  }

  public ApiException toApiException() {
    return new ApiException(getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
