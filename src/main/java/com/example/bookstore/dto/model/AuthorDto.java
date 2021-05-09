package com.example.bookstore.dto.model;

import com.example.bookstore.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
public class AuthorDto {
  Long id;
  String email;
  @JsonFormat(pattern = Constants.DATE_FORMAT)
  Date dateOfBirth;

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class Request extends AuthorDto {
    String firstName;
    String lastName;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class Response extends AuthorDto {
    String firstName;
    String lastName;
    Date createdAt;
    Date updatedAt;
  }

  @EqualsAndHashCode(callSuper = true)
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
