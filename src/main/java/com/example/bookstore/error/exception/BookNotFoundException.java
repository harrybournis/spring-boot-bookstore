package com.example.bookstore.error.exception;

public class BookNotFoundException extends ResourceNotFoundException {
  public BookNotFoundException(Long id) {
    super("book.not_found", new Object[] {id});
  }
}
