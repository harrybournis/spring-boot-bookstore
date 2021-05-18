package com.example.bookstore.factories;

import com.example.bookstore.entity.Publisher;

public class PublisherFactory extends BaseFactory {
  public static Publisher.PublisherBuilder builder() {
    return Publisher
            .builder()
            .name("name " + randomString()).address("address");
  }

  public static Publisher build() {
    return builder().build();
  }
}
