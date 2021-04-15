package com.example.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AuthorDto {
  Long id;
  String email;

  @Data
  public static class Request extends AuthorDto {
    String firstName;
    String lastName;
  }

  @Data
  public static class Response extends AuthorDto {
    String firstName;
    String lastName;
  }

  @Data
  public static class ResponseNested extends AuthorDto {
    @JsonIgnore
    String firstName;
    @JsonIgnore
    String lastName;
    String fullName;

    public String getFullName() {
      return firstName + " " + lastName;
    }
  }
}
