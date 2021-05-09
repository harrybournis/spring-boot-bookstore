package com.example.bookstore.entity;

import com.example.bookstore.UnitTest;
import com.example.bookstore.factories.PublisherFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PublisherTest extends UnitTest {
  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Publisher subject() {
    return PublisherFactory.build();
  }

  private void validate(Publisher publisher, String property, String message) {
    Set<ConstraintViolation<Publisher>> violations = validator.validate(publisher);
    ConstraintViolation<Publisher> violation = violations.iterator().next();
    assertEquals(property, violation.getPropertyPath().toString());
    assertEquals(message, violation.getMessage());
  }

  private String generateRandomString(int length) {
    return RandomStringUtils.randomAlphabetic(length);
  }

  @Test
  @DisplayName("valid")
  void testValid() {
    Publisher publisher = subject();
    Set<ConstraintViolation<Publisher>> violations = validator.validate(publisher);
    assertEquals(0, violations.size());
  }

  @Nested
  class Name {
    @Test
    @DisplayName("name blank")
    void testInvalid1() {
      Publisher publisher = subject();
      publisher.setName(null);
      validate(publisher, "name", "must not be blank");
    }

    @Test
    @DisplayName("name invalid size")
    void testInvalid2() {
      Publisher publisher = subject();
      publisher.setName(generateRandomString(51));
      validate(publisher, "name", "size must be between 0 and 50");
    }
  }

  @Nested
  class Address {
    @Test
    @DisplayName("address blank")
    void testInvalid3() {
      Publisher publisher = subject();
      publisher.setAddress(null);
      validate(publisher, "address", "must not be blank");
    }

    @Test
    @DisplayName("address invalid size")
    void testInvalid4() {
      Publisher publisher = subject();
      publisher.setAddress(generateRandomString(101));
      validate(publisher, "address", "size must be between 0 and 100");
    }
  }
}