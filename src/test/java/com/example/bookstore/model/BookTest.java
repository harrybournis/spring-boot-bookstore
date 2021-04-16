package com.example.bookstore.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {
  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Book subject() {
    return Book
            .builder()
            .id(1L).isbn("0-5960-6438-1").title("title").description("description").visible(true).releaseDate(new Date())
            .author(Author.builder().id(1L).build())
            .build();
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
  public void testValid() {
    Book book = subject();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertEquals(0, violations.size());
  }

  @Test
  @DisplayName("title blank")
  public void testInvalid1() {
    Book book = subject();
    book.setTitle(null);
    validate(book, "title", "must not be blank");
  }

  @Test
  @DisplayName("title invalid size")
  public void testInvalid2() {
    Book book = subject();
    book.setTitle(generateRandomString(101));
    validate(book, "title", "size must be between 0 and 100");
  }

  @Test
  @DisplayName("description invalid size")
  public void testInvalid4() {
    Book book = subject();
    book.setDescription(generateRandomString(1001));
    validate(book, "description", "size must be between 0 and 1000");
  }

  @Test
  @DisplayName("visible missing")
  public void testInvalid5() {
    Book book = subject();
    book.setVisible(null);
    validate(book, "visible", "must not be null");
  }

  @Test
  @DisplayName("author missing")
  public void testInvalid6() {
    Book book = subject();
    book.setAuthor(null);
    validate(book, "author", "must not be null");
  }

  @Test
  @DisplayName("isbn blank")
  public void testInvalid7() {
    Book book = subject();
    book.setIsbn(null);
    validate(book, "isbn", "must not be blank");
  }

  @Test
  @DisplayName("isbn invalid format")
  public void testInvalid9() {
    Book book = subject();
    book.setIsbn("djk329d8");
    validate(book, "isbn", "invalid ISBN");
  }

  @Test
  @DisplayName("isbn works with multiple isbn formats")
  public void testInvalid10() {
    Book book = subject();
    book.setIsbn("0-7646-2830-5");
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertEquals(0, violations.size());
    book.setIsbn("978-0-0889-2304-0");
    violations = validator.validate(book);
    assertEquals(0, violations.size());
  }
}