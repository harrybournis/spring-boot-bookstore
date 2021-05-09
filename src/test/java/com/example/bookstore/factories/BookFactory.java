package com.example.bookstore.factories;

import com.example.bookstore.entity.Book;

import java.util.Date;

public class BookFactory {
  public static Book.BookBuilder builder() {
    return Book
            .builder()
            .isbn("0-5960-6438-1").title("title").description("description").visible(true).releaseDate(new Date())
            .author(AuthorFactory.build())
            .publisher(PublisherFactory.build());
  }

  public static Book build() {
    return builder().build();
  }
}
