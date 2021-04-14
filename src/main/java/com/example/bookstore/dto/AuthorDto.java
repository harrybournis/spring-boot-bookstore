package com.example.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class AuthorDto {
  Long id;
  String firstName;
  String lastName;
  String email;

  @JsonIgnoreProperties({"firstName", "lastName"})
  public static class WithFullName extends AuthorDto {
    String fullName;

    public String getFullName() {
      return firstName + " " + lastName;
    }
  }
}
