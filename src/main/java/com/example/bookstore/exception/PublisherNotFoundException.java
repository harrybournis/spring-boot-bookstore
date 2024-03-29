package com.example.bookstore.exception;

public class PublisherNotFoundException extends ResourceNotFoundException {
  public PublisherNotFoundException(Long id) {
    super("publisher.not_found", new Object[] {id});
  }
}
