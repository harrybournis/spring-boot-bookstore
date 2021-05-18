package com.example.bookstore.dto.entity;

import com.example.bookstore.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
public class BookDto {
  Long id;
  String title;
  String description;
  String isbn;
  Boolean visible;
  @JsonFormat(pattern = Constants.DATE_FORMAT)
  Date releaseDate;
  Integer position;

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class Request extends BookDto {
    Long authorId;
    Long publisherId;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class Response extends BookDto {
    AuthorDto.ResponseNested author;
    PublisherDto.ResponseNested publisher;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class ResponseList extends BookDto {
    private static int descriptionLength = 100;
    AuthorDto.ResponseNested author;

    @Override
    public String getDescription() {
      if (description == null || description.length() < descriptionLength)
        return description;

      return description.substring(0, descriptionLength) + "...";
    }
  }
}
