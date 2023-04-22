package com.example.bookstore.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import com.example.bookstore.UnitTest;
import com.example.bookstore.factories.BookFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class BookTest extends UnitTest {
  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Book subject() {
    return BookFactory.build();
  }

  private void validate(Book book, String property, String message) {
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    ConstraintViolation<Book> violation = violations.iterator().next();
    assertEquals(property, violation.getPropertyPath().toString());
    assertEquals(message, violation.getMessage());
  }

  private String generateRandomString(int length) {
    return RandomStringUtils.randomAlphabetic(length);
  }

  @Test
  @DisplayName("valid")
  void testValid() {
    Book book = subject();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertEquals(0, violations.size());
  }

  @Nested
  class Title {
    @Test
    @DisplayName("title blank")
    void testInvalid1() {
      Book book = subject();
      book.setTitle(null);
      validate(book, "title", "may not be empty");
    }

    @Test
    @DisplayName("title invalid size")
    void testInvalid2() {
      Book book = subject();
      book.setTitle(generateRandomString(101));
      validate(book, "title", "size must be between 0 and 100");
    }
  }

  @Nested
  class Description {
    @Test
    @DisplayName("description invalid size")
    void testInvalid4() {
      Book book = subject();
      book.setDescription(generateRandomString(1001));
      validate(book, "description", "size must be between 0 and 1000");
    }
  }

  @Nested
  class Visible {
    @Test
    @DisplayName("visible missing")
    void testInvalid5() {
      Book book = subject();
      book.setVisible(null);
      validate(book, "visible", "must not be null");
    }
  }

  @Nested
  @DisplayName("Author")
  class AuthorValidations {
    @Test
    @DisplayName("author missing")
    void testInvalid6() {
      Book book = subject();
      book.setAuthor(null);
      validate(book, "author", "must not be null");
    }
  }

  @Nested
  class Isbn {
    @Test
    @DisplayName("isbn blank")
    void testInvalid7() {
      Book book = subject();
      book.setIsbn(null);
      validate(book, "isbn", "may not be empty");
    }

    @Test
    @DisplayName("isbn invalid format")
    void testInvalid9() {
      Book book = subject();
      book.setIsbn("djk329d8");
      validate(book, "isbn", "invalid ISBN");
    }

    @Test
    @DisplayName("isbn works with multiple isbn formats")
    void testInvalid10() {
      Book book = subject();
      book.setIsbn("0-7646-2830-5");
      Set<ConstraintViolation<Book>> violations = validator.validate(book);
      assertEquals(0, violations.size());
      book.setIsbn("978-0-0889-2304-0");
      violations = validator.validate(book);
      assertEquals(0, violations.size());
    }
  }
}
