package com.example.bookstore.factories;

import com.example.bookstore.model.Author;

import java.util.Date;

public class AuthorFactory {
  public static Author.AuthorBuilder builder() {
    return Author
            .builder()
            .firstName("first").lastName("last").email("email@gmail.com").dateOfBirth(new Date());
  }

  public static Author build() {
    return builder().build();
  }
}
