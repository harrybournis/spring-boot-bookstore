package com.example.bookstore.error;

import org.springframework.http.HttpStatus;

public class PublisherNotFoundException extends ApiException {
  public PublisherNotFoundException(Long id) {
    super("publisher.not_found", HttpStatus.BAD_REQUEST, new Object[] {id});
  }
}
