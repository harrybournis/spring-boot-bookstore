package com.example.bookstore.factories;

import org.apache.commons.lang3.RandomStringUtils;

public class BaseFactory {
  protected static String randomString() {
      return RandomStringUtils.randomAlphabetic(6);
  }
}
