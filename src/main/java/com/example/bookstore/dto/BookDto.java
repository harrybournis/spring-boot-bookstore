package com.example.bookstore.dto;

import lombok.Data;

@Data
public class BookDto {
  Long id;
  String title;
  String description;
  String isbn;
  Boolean visible;
  String position;

  @Data
  public static class Request extends BookDto {
    Long authorId;
    Long publisherId;
  }

  @Data
  public static class Response extends BookDto {
    AuthorDto.ResponseNested author;
    PublisherDto publisher;
  }

  @Data
  public static class ResponseList extends Response {
    private static int descriptionLength = 100;

    @Override
    public String getDescription() {
      if (description == null || description.length() < descriptionLength)
        return description;

      return description.substring(0, descriptionLength) + "...";
    }
  }
}
