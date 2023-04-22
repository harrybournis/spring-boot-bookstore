package com.example.bookstore.controller;

import com.example.bookstore.entity.Publisher;
import com.example.bookstore.factories.PublisherFactory;
import com.example.bookstore.repository.PublisherRepository;
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
public class PublisherControllerTest {
  @Autowired
  PublisherRepository publisherRepository;

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8181;
  }

  @BeforeEach
  void cleanDatabase() {
    publisherRepository.deleteAll();
  }

  @Nested
  @DisplayName("GET /publishers")
  class GetAll {
    @DisplayName("returns publishers")
    @Test
    void getAll() throws Exception {
      Publisher publisher = PublisherFactory.builder().name("publisher name").build();
      publisherRepository.saveAndFlush(publisher);

      get("/api/v1/publishers")
              .then()
              .body("size()", equalTo(1))
              .body("id", hasItems(publisher.getId().intValue()))
              .body("name", hasItems(publisher.getName()))
              .statusCode(200);
    }
  }

  @Nested
  @DisplayName("GET /publishers/:id")
  class GetOne {
    @Test
    @DisplayName("returns one when it exists")
    void getOne() throws Exception {
      Publisher publisher = PublisherFactory.builder().build();
      publisherRepository.saveAndFlush(publisher);

      get("/api/v1/publishers/" + publisher.getId())
              .then()
              .body("id", equalTo(publisher.getId().intValue()))
              .body("name", equalTo(publisher.getName()))
              .body("createdAt", notNullValue())
              .body("updatedAt", notNullValue())
              .statusCode(200);
    }

    @Test
    @DisplayName("returns error when publisher does not exist")
    void getOneError() throws Exception {
      get("/api/v1/publishers/0")
              .then()
              .statusCode(404)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("publisher.not_found"))
              .body("errors.message", hasItem("Publisher with provided id 0 not found."));
    }
  }

  @Nested
  @DisplayName("POST /publishers")
  class Create {
    @Test
    @DisplayName("creates publisher")
    void crate() {
      Map<String, String> params = new HashMap<>();
      params.put("name", "Publihser name");
      params.put("address", "address1");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/publishers")
              .then()
              .body("name", equalTo(params.get("name")))
              .body("address", equalTo(params.get("address")))
              .body("createdAt", notNullValue())
              .body("updatedAt", notNullValue())
              .statusCode(200);

      assert(publisherRepository.findByName(params.get("name")).isPresent());
    }

    @Test
    @DisplayName("raises error when validation error")
    void crateError() {
      Map<String, String> params = new HashMap<>();
      params.put("address", "address1");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/publishers")
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("publisher.name.may_not_be_empty"))
          .body("errors.message", hasItem("may not be empty"));

      assertThat(publisherRepository.findByName(params.get("name")).isPresent(), equalTo(false));
    }

    @Test
    @DisplayName("raises error when name uniqueness error")
    void crateErrorUnique() {
      String name = "name that exists";
      publisherRepository.saveAndFlush(PublisherFactory.builder().name(name).build());

      Map<String, String> params = new HashMap<>();
      params.put("name", name);
      params.put("address", "address");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/publishers")
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("publisher.name.must_be_unique"))
              .body("errors.message", hasItem("Name must be unique."));
    }
  }

  @Nested
  @DisplayName("PATCH /publishers/:id")
  class Update {
    Publisher publisher;

    @BeforeEach
    void createPublisher() {
      publisher = publisherRepository.saveAndFlush(PublisherFactory.builder().build());
    }

    @Test
    @DisplayName("updates publisher")
    void update() {
      String newName = "new neame name";
      Map<String, String> params = new HashMap<>();
      params.put("name", newName);

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .patch("/api/v1/publishers/" + publisher.getId())
              .then()
              .body("name", equalTo(newName))
              .body("address", equalTo(publisher.getAddress()))
              .body("createdAt", notNullValue())
              .body("updatedAt", notNullValue())
              .statusCode(200);
    }

    @Test
    @DisplayName("raises error when validation error")
    void updateError() {
      Map<String, String> params = new HashMap<>();
      params.put("address", "");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .patch("/api/v1/publishers/" + publisher.getId())
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("publisher.address.may_not_be_empty"))
          .body("errors.message", hasItem("may not be empty"));
    }

    @Test
    @DisplayName("raises error when name uniqueness error")
    void updateErrorUnique() {
      String name = "name2";
      publisherRepository.saveAndFlush(PublisherFactory.builder().name(name).build());

      Map<String, String> params = new HashMap<>();
      params.put("name", name);

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .patch("/api/v1/publishers/" + publisher.getId())
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("publisher.name.must_be_unique"))
              .body("errors.message", hasItem("Name must be unique."));
    }
  }
}
