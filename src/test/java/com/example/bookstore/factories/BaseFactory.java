package com.example.bookstore.factories;

import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

public class BaseFactory {
  protected static String randomString() {
    return RandomStringUtils.randomAlphabetic(6);
  }
}
