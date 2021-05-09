package com.example.bookstore.controller;

import com.example.bookstore.factories.AuthorFactory;
import com.example.bookstore.entity.Author;
import com.example.bookstore.repository.AuthorRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorControllerTest {
  @Autowired
  AuthorRepository authorRepository;

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8181;
  }

  @BeforeEach
  void cleanDatabase() {
    authorRepository.deleteAll();
  }

  @Nested
  @DisplayName("GET /authors")
  class GetAll {
    @DisplayName("returns authors")
    @Test
    void getAll() throws Exception {
      Author author = AuthorFactory.builder().email("aaaaa@aaaa.com").build();
      authorRepository.saveAndFlush(author);

      get("/api/v1/authors")
              .then()
              .body("size()", equalTo(1))
              .body("id", hasItems(author.getId().intValue()))
              .body("email", hasItems(author.getEmail()))
              .statusCode(200);
    }
  }

  @Nested
  @DisplayName("GET /authors/:id")
  class GetOne {
    @Test
    @DisplayName("returns one when it exists")
    void getOne() throws Exception {
      Author author = AuthorFactory.builder().email("aaaaa@aaaa.com").build();
      authorRepository.saveAndFlush(author);

      get("/api/v1/authors/" + author.getId())
              .then()
              .body("id", equalTo(author.getId().intValue()))
              .body("email", equalTo(author.getEmail()))
              .statusCode(200);
    }

    @Test
    @DisplayName("returns error when author does not exist")
    void getOneError() throws Exception {
      get("/api/v1/authors/0")
              .then()
              .statusCode(404)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("author.not_found"))
              .body("errors.message", hasItem("Author with provided id 0 not found."));
    }
  }

  @Nested
  @DisplayName("POST /authors")
  class Create {
    @Test
    @DisplayName("creates author")
    void crate() {
      Map<String, String> params = new HashMap<>();
      params.put("firstName", "first");
      params.put("lastName", "last");
      params.put("email", "dsdssssimceidaadaldd@gamil.com");
      params.put("dateOfBirth", "1956-02-24");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/authors")
              .then()
              .body("firstName", equalTo(params.get("firstName")))
              .body("lastName", equalTo(params.get("lastName")))
              .body("email", equalTo(params.get("email")))
              .body("dateOfBirth", equalTo(params.get("dateOfBirth")))
              .body("createdAt", notNullValue())
              .body("updatedAt", notNullValue())
              .statusCode(200);

      assert(authorRepository.findByEmail(params.get("email")).isPresent());
    }

    @Test
    @DisplayName("raises error when validation error")
    void crateError() {
      Map<String, String> params = new HashMap<>();
      params.put("firstName", "first");
      params.put("lastName", "last");
      params.put("dateOfBirth", "1956-02-24");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/authors")
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("author.email.must_not_be_blank"))
              .body("errors.message", hasItem("must not be blank"));

      assertThat(authorRepository.findByEmail(params.get("email")).isPresent(), equalTo(false));
    }

    @Test
    @DisplayName("raises error when email uniqueness error")
    void crateErrorUnique() {
      String email = "email@exists.com";
      authorRepository.saveAndFlush(AuthorFactory.builder().email(email).build());

      Map<String, String> params = new HashMap<>();
      params.put("firstName", "first");
      params.put("lastName", "last");
      params.put("email", email);
      params.put("dateOfBirth", "1956-02-24");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/authors")
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("author.email.must_be_unique"))
              .body("errors.message", hasItem("Email must be unique."));
    }
  }

  @Nested
  @DisplayName("PATCH /authors/:id")
  class Update {
    Author author;

    @BeforeEach
    void createAuthor() {
      author = authorRepository.saveAndFlush(AuthorFactory.builder().build());
    }

    @Test
    @DisplayName("updates author")
    void update() {
      String newFirstName = "new first name";
      Map<String, String> params = new HashMap<>();
      params.put("firstName", newFirstName);

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .patch("/api/v1/authors/" + author.getId())
              .then()
              .body("firstName", equalTo(newFirstName))
              .body("lastName", equalTo(author.getLastName()))
              .body("email", equalTo(author.getEmail()))
              .body("dateOfBirth", notNullValue())
              .body("createdAt", notNullValue())
              .body("updatedAt", notNullValue())
              .statusCode(200);
    }

    @Test
    @DisplayName("raises error when validation error")
    void updateError() {
      Map<String, String> params = new HashMap<>();
      params.put("firstName", "");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .patch("/api/v1/authors/" + author.getId())
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("author.firstName.must_not_be_blank"))
              .body("errors.message", hasItem("must not be blank"));
    }

    @Test
    @DisplayName("raises error when email uniqueness error")
    void updateErrorUnique() {
      String email = "email@exists.com";
      authorRepository.saveAndFlush(AuthorFactory.builder().email(email).build());

      Map<String, String> params = new HashMap<>();
      params.put("email", email);

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .patch("/api/v1/authors/" + author.getId())
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("author.email.must_be_unique"))
              .body("errors.message", hasItem("Email must be unique."));
    }
  }
}