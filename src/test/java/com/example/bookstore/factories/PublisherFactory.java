package com.example.bookstore.factories;

import com.example.bookstore.model.Publisher;

public class PublisherFactory {
  public static Publisher.PublisherBuilder builder() {
    return Publisher
            .builder()
            .name("name").address("address");
  }

  public static Publisher build() {
    return builder().build();
  }
}
