package com.example.bookstore.entity;

import com.example.bookstore.UnitTest;
import com.example.bookstore.factories.AuthorFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorTest extends UnitTest {
  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Author.AuthorBuilder subject() {
    return AuthorFactory.builder();
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
  void testValid() {
    Author author = subject().build();
    Set<ConstraintViolation<Author>> violations = validator.validate(author);
    assertEquals(0, violations.size());
  }

  @Nested
  class FirstName {
    @Test
    @DisplayName("firstName null")
    void testInvalid1() {
      Author author = subject().firstName(null).build();
      validate(author, "firstName", "must not be blank");
    }

    @Test
    @DisplayName("firstName empty string")
    void testInvalidEmptyString() {
      Author author = subject().firstName("").build();
      validate(author, "firstName", "must not be blank");
    }

    @Test
    @DisplayName("firstName invalid size")
    void testInvalid2() {
      Author author = subject().firstName(generateRandomString(51)).build();
      validate(author, "firstName", "size must be between 0 and 50");
    }
  }

  @Nested
  class LastName {
    @Test
    @DisplayName("lastName blank")
    void testInvalid3() {
      Author author = subject().lastName(null).build();
      validate(author, "lastName", "must not be blank");
    }

    @Test
    @DisplayName("lastName invalid size")
    void testInvalid4() {
      Author author = subject().lastName(generateRandomString(51)).build();
      validate(author, "lastName", "size must be between 0 and 50");
    }
  }

  @Nested
  class Email {
    @Test
    @DisplayName("email blank")
    void testInvalid5() {
      Author author = subject().email(null).build();
      validate(author, "email", "must not be blank");
    }

    @Test
    @DisplayName("email invalid size")
    void testInvalid6() {
      Author author = subject().email(generateRandomString(51) + "@gmail.com").build();
      validate(author, "email", "size must be between 0 and 50");
    }

    @Test
    @DisplayName("email invalid format")
    void testInvalid7() {
      Author author = subject().email("invalid string").build();
      validate(author, "email", "invalid");
    }
  }
}