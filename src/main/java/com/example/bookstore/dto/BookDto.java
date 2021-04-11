package com.example.bookstore.dto;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Publisher;
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
  Author author;
  Publisher publisher;
}
