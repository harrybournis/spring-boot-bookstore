package com.example.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookDto {
  Long id;
  String title;
  String description;
  String isbn;
  Boolean visible;
  String position;
  Long authorId;
  Long publisherId;
  @JsonProperty("author")
  AuthorDto authorDto;
  @JsonProperty("publisher")
  PublisherDto publisherDto;
}
