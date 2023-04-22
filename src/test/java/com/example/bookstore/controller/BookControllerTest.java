package com.example.bookstore.controller;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Publisher;
import com.example.bookstore.factories.AuthorFactory;
import com.example.bookstore.factories.BookFactory;
import com.example.bookstore.factories.PublisherFactory;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
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
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasItems;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookControllerTest {
  @Autowired
  BookRepository bookRepository;

  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  PublisherRepository publisherRepository;

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8181;
  }

  @BeforeEach
  void cleanDatabase() {
    bookRepository.deleteAll();
    authorRepository.deleteAll();
    publisherRepository.deleteAll();
  }

  @Nested
  @DisplayName("GET /books")
  class GetAll {
    @DisplayName("returns books")
    @Test
    void getAll() throws Exception {
      Author author = AuthorFactory.build();
      authorRepository.saveAndFlush(author);
      Publisher publisher = PublisherFactory.build();
      publisherRepository.saveAndFlush(publisher);
      Book book = BookFactory
              .builder()
              .isbn(BookFactory.VALID_ISBN[1])
              .author(author)
              .publisher(publisher)
              .build();
      bookRepository.saveAndFlush(book);

      get("/api/v1/books")
              .then()
              .body("size()", equalTo(1))
              .body("id", hasItems(book.getId().intValue()))
              .body("title", hasItems(book.getTitle()))
              .body("description", hasItems(book.getDescription()))
              .body("isbn", hasItems(book.getIsbn()))
              .body("releaseDate", notNullValue())
              .body("visible", hasItems(book.getVisible()))
              .body("position", hasItems(book.getPosition()))
              .body("author.id", hasItems(book.getAuthor().getId().intValue()))
              .body("author.fullName", hasItems(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName()))
              .statusCode(200);
    }
  }

  @Nested
  @DisplayName("GET /books/:id")
  class GetOne {
    @Test
    @DisplayName("returns one when it exists")
    void getOne() throws Exception {
      Author author = AuthorFactory.build();
      authorRepository.saveAndFlush(author);
      Publisher publisher = PublisherFactory.build();
      publisherRepository.saveAndFlush(publisher);
      Book book = BookFactory
              .builder()
              .author(author)
              .publisher(publisher)
              .build();
      bookRepository.saveAndFlush(book);

      get("/api/v1/books/" + book.getId())
              .then()
              .body("id", equalTo(book.getId().intValue()))
              .body("title", equalTo(book.getTitle()))
              .body("description", equalTo(book.getDescription()))
              .body("isbn", equalTo(book.getIsbn()))
              .body("releaseDate", notNullValue())
              .body("visible", equalTo(book.getVisible()))
              .body("position", equalTo(book.getPosition()))
              .body("author.id", equalTo(book.getAuthor().getId().intValue()))
              .body("author.fullName", equalTo(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName()))
              .body("publisher.id", equalTo(book.getPublisher().getId().intValue()))
              .body("publisher.name", equalTo(book.getPublisher().getName()))
              .body("publisher.address", equalTo(book.getPublisher().getAddress()))
              .statusCode(200);
    }

    @Test
    @DisplayName("returns error when book does not exist")
    void getOneError() throws Exception {
      get("/api/v1/books/0")
              .then()
              .statusCode(404)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("book.not_found"))
              .body("errors.message", hasItem("Book with provided id 0 not found."));
    }
  }

  @Nested
  @DisplayName("POST /books")
  class Create {
    @Test
    @DisplayName("creates book")
    void create() {
      Author author = AuthorFactory.build();
      authorRepository.saveAndFlush(author);
      Publisher publisher = PublisherFactory.build();
      publisherRepository.saveAndFlush(publisher);

      Map<String, String> params = new HashMap<>();
      params.put("title", "Book title");
      params.put("description", "book description");
      params.put("isbn", BookFactory.VALID_ISBN[0]);
      params.put("authorId", author.getId().toString());
      params.put("publisherId", publisher.getId().toString());
      params.put("position", "1");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/books")
              .then()
              .body("id", notNullValue())
              .body("title", equalTo(params.get("title")))
              .body("description", equalTo(params.get("description")))
              .body("isbn", equalTo(params.get("isbn")))
              .body("releaseDate", nullValue())
              .body("visible", equalTo(true))
              .body("position", equalTo(1))
              .body("author.id", equalTo(author.getId().intValue()))
              .body("author.fullName", equalTo(author.getFirstName() + " " + author.getLastName()))
              .body("publisher.id", equalTo(publisher.getId().intValue()))
              .body("publisher.name", equalTo(publisher.getName()))
              .body("publisher.address", equalTo(publisher.getAddress()))
              .statusCode(200);

      assert(bookRepository.findByIsbn(params.get("isbn")).isPresent());
    }

    @Test
    @DisplayName("raises error when validation error")
    void crateError() {
      Author author = AuthorFactory.build();
      authorRepository.saveAndFlush(author);

      Map<String, String> params = new HashMap<>();
      params.put("description", "book description");
      params.put("isbn", BookFactory.VALID_ISBN[0]);
      params.put("authorId", author.getId().toString());

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/books")
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("book.title.may_not_be_empty"))
          .body("errors.message", hasItem("may not be empty"));

      assert(bookRepository.findByIsbn(params.get("isbn")).isEmpty());
    }

    @Test
    @DisplayName("raises error when isbn uniqueness error")
    void crateErrorUnique() {
      Author author = AuthorFactory.build();
      authorRepository.saveAndFlush(author);

      String isbn = BookFactory.VALID_ISBN[0];
      bookRepository.saveAndFlush(BookFactory.builder().isbn(isbn).author(author).build());

      Map<String, String> params = new HashMap<>();
      params.put("title", "Book title");
      params.put("isbn", isbn);
      params.put("authorId", author.getId().toString());

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/books")
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("book.isbn.must_be_unique"))
              .body("errors.message", hasItem("Isbn must be unique."));
    }

    @Test
    @DisplayName("raises error when references author does not exist")
    void crateErrorNoAuthor() {
      Map<String, String> params = new HashMap<>();
      params.put("title", "Book title");
      params.put("isbn", BookFactory.VALID_ISBN[0]);
      params.put("authorId", "0");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .post("/api/v1/books")
              .then()
              .statusCode(404)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("author.not_found"))
              .body("errors.message", hasItem("Author with provided id 0 not found."));
    }
  }

  @Nested
  @DisplayName("PATCH /books/:id")
  class Update {
    Author author;
    Book book;

    @BeforeEach
    void createBook() {
      author = AuthorFactory.build();
      author = authorRepository.saveAndFlush(author);
      book = bookRepository.saveAndFlush(BookFactory.builder().author(author).build());
    }

    @Test
    @DisplayName("updates book")
    void update() {
      Author otherAuthor = AuthorFactory.builder().firstName("other").lastName("author").build();
      authorRepository.saveAndFlush(otherAuthor);

      String newTitle = "new title";
      Map<String, String> params = new HashMap<>();
      params.put("title", newTitle);
      params.put("authorId", otherAuthor.getId().toString());

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .patch("/api/v1/books/" + book.getId())
              .then()
              .body("title", equalTo(newTitle))
              .body("description", equalTo(book.getDescription()))
              .body("author.id", equalTo(otherAuthor.getId().intValue()))
              .statusCode(200);
    }

    @Test
    @DisplayName("raises error when validation error")
    void updateError() {
      Map<String, String> params = new HashMap<>();
      params.put("title", "");

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .patch("/api/v1/books/" + book.getId())
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("book.title.may_not_be_empty"))
          .body("errors.message", hasItem("may not be empty"));
    }

    @Test
    @DisplayName("raises error when email uniqueness error")
    void updateErrorUnique() {
      String isbn = BookFactory.VALID_ISBN[2];
      bookRepository.saveAndFlush(BookFactory.builder().author(author).isbn(isbn).build());

      Map<String, String> params = new HashMap<>();
      params.put("isbn", isbn);

      given()
              .body(params)
              .contentType(ContentType.JSON)
              .patch("/api/v1/books/" + book.getId())
              .then()
              .statusCode(400)
              .body("timestamp", notNullValue())
              .body("errors.code", hasItem("book.isbn.must_be_unique"))
              .body("errors.message", hasItem("Isbn must be unique."));
    }
  }
}
