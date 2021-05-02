package com.example.bookstore.controller;

import com.example.bookstore.factories.AuthorFactory;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.model.Author;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.service.AuthorService;
import io.restassured.RestAssured;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorControllerTest {
  @Tested
  AuthorController authorController;

  @Mocked
  AuthorRepository authorRepository;

  @Injectable
  AuthorService authorService;

  @Injectable
  AuthorMapper authorMapper;

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8181;
  }

  @DisplayName("getAll returns authors")
  @Test
  @Transactional
  void getAll() throws Exception {
    Author author = AuthorFactory.builder().email("aaaaa@aaaa.com").build();

//    when(authorRepository.findAll()).thenReturn(List.of(author));
    new Expectations() {{
      authorRepository.findAll();
      result = List.of(author);
    }};

    String json = get("/api/v1/authors").asString();
    assertThat(json, equalTo("aaa"));
//    get("/api/v1/authors")
//            .then()
//            .body("size()", equalTo(1))
//            .body("id", hasItems(1))
//            .body("email", hasItems(author.getEmail()))
//            .statusCode(200);
  }

//  @Test
//  @DisplayName("GET /authors/:id returns one when it exists")
//  void getOne() throws Exception {
//    Author author = AuthorFactory.builder().email("aaaaa@aaaa.com").build();
//    when(authorRepository.findOne(author.getId())).thenReturn(List.of(author));
//
//    get("/api/v1/authors/${author.id}")
//            .then()
//            .body("size()", equalTo(1))
//            .body("id", equalTo(author.getId()))
//            .body("email", equalTo(author.getEmail()))
//            .statusCode(200);
//  }

//  @Test
//  void find() {
//  }
//
//  @Test
//  void create() {
//  }
//
//  @Test
//  void update() {
//  }
//
//  @Test
//  void delete() {
//  }
}