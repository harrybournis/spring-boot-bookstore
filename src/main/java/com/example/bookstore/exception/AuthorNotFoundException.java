package com.example.bookstore.exception;

public class AuthorNotFoundException extends ResourceNotFoundException {
  public AuthorNotFoundException(Long id) {
    super("author.not_found", new Object[] {id});
  }
}
