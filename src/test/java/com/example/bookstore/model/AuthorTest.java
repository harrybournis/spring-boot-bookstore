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

class AuthorTest {
  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Author subject() {
    return Author
            .builder()
            .id(1L).firstName("first").lastName("last").email("email@gmail.com").dateOfBirth(new Date())
            .build();
  }

  private void validate(Author author, String property, String message) {
    Set<ConstraintViolation<Author>> violations = validator.validate(author);
    ConstraintViolation<Author> violation = violations.iterator().next();
    assertEquals(property, violation.getPropertyPath().toString());
    assertEquals(message, violation.getMessage());
  }

  private String generateRandomString(int length) {
    return RandomStringUtils.randomAlphabetic(length);
  }

  @Test
  @DisplayName("valid")
  public void testValid() {
    Author author = subject();
    Set<ConstraintViolation<Author>> violations = validator.validate(author);
    assertEquals(0, violations.size());
  }

  @Test
  @DisplayName("firstName blank")
  public void testInvalid1() {
    Author author = subject();
    author.setFirstName(null);
    validate(author, "firstName", "must not be blank");
  }

  @Test
  @DisplayName("firstName invalid size")
  public void testInvalid2() {
    Author author = subject();
    author.setFirstName(generateRandomString(51));
    validate(author, "firstName", "size must be between 0 and 50");
  }

  @Test
  @DisplayName("lastName blank")
  public void testInvalid3() {
    Author author = subject();
    author.setLastName(null);
    validate(author, "lastName", "must not be blank");
  }

  @Test
  @DisplayName("lastName invalid size")
  public void testInvalid4() {
    Author author = subject();
    author.setLastName(generateRandomString(51));
    validate(author, "lastName", "size must be between 0 and 50");
  }

  @Test
  @DisplayName("email blank")
  public void testInvalid5() {
    Author author = subject();
    author.setEmail(null);
    validate(author, "email", "must not be blank");
  }

  @Test
  @DisplayName("email invalid size")
  public void testInvalid6() {
    Author author = subject();
    author.setEmail(generateRandomString(51));
    validate(author, "email", "size must be between 0 and 50");
  }

  @Test
  @DisplayName("email invalid format")
  public void testInvalid7() {
    Author author = subject();
    author.setEmail("invalid string");
    validate(author, "email", "invalid");
  }
}