package com.example.bookstore.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PublisherTest {
  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Publisher subject() {
    return Publisher
            .builder()
            .id(1L).name("name").address("address")
            .build();
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
  public void testValid() {
    Publisher publisher = subject();
    Set<ConstraintViolation<Publisher>> violations = validator.validate(publisher);
    assertEquals(0, violations.size());
  }

  @Test
  @DisplayName("name blank")
  public void testInvalid1() {
    Publisher publisher = subject();
    publisher.setName(null);
    validate(publisher, "name", "must not be blank");
  }

  @Test
  @DisplayName("name invalid size")
  public void testInvalid2() {
    Publisher publisher = subject();
    publisher.setName(generateRandomString(51));
    validate(publisher, "name", "size must be between 0 and 50");
  }

  @Test
  @DisplayName("address blank")
  public void testInvalid3() {
    Publisher publisher = subject();
    publisher.setAddress(null);
    validate(publisher, "address", "must not be blank");
  }

  @Test
  @DisplayName("address invalid size")
  public void testInvalid4() {
    Publisher publisher = subject();
    publisher.setAddress(generateRandomString(101));
    validate(publisher, "address", "size must be between 0 and 100");
  }
}