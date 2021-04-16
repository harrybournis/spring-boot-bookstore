package com.example.bookstore.exception;

public class BookNotFoundException extends ResourceNotFoundException {
  public BookNotFoundException(Long id) {
    super("book.not_found", new Object[] {id});
  }
}
