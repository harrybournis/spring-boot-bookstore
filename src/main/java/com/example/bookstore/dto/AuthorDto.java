package com.example.bookstore.dto;

import lombok.Data;

@Data
public class AuthorDto {
  Long id;
  String firstName;
  String lastName;
  String email;
}
