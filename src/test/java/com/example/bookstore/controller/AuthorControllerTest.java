package com.example.bookstore.controller;

import com.example.bookstore.IntegrationTest;
import com.example.bookstore.factories.AuthorFactory;
import com.example.bookstore.model.Author;
import com.example.bookstore.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthorControllerTest extends IntegrationTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  AuthorRepository authorRepository;

  @Test
  @DisplayName("getAll returns authors")
  void getAll() throws Exception {
    Author author = AuthorFactory.builder().build();
    authorRepository.save(author);

    mockMvc.perform(get("/api/v1/authors")
            .contentType("application/json"))
            .andExpect(status().isOk());
  }

  @Test
  void find() {
  }

  @Test
  void create() {
  }

  @Test
  void update() {
  }

  @Test
  void delete() {
  }
}