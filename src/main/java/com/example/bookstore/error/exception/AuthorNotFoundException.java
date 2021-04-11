package com.example.bookstore.error.exception;

import org.springframework.http.HttpStatus;

public class AuthorNotFoundException extends ApiException {
  public AuthorNotFoundException(Long id) {
    super("author.not_found", HttpStatus.BAD_REQUEST, new Object[] {id});
  }
}
