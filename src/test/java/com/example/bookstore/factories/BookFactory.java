package com.example.bookstore.factories;

import com.example.bookstore.entity.Book;

import java.util.Date;

public class BookFactory extends BaseFactory {
  public static String[] VALID_ISBN = {
          "0-5960-6438-1",
          "0-3188-9064-X",
          "0-7946-3588-1",
          "978-8-6443-8869-2",
          "978-7-3347-2559-5"
  };

  public static Book.BookBuilder builder() {
    return Book
            .builder()
            .isbn(VALID_ISBN[0]).title("title").description("description").visible(true).releaseDate(new Date())
            .author(AuthorFactory.build());
  }

  public static Book build() {
    return builder().build();
  }
}
