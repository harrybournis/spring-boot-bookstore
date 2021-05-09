package com.example.bookstore.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
public class PublisherDto {
  Long id;
  String name;
  String address;

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class Request extends PublisherDto {
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class Response extends PublisherDto {
    Date createdAt;
    Date updatedAt;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class ResponseNested extends PublisherDto {
  }
}
