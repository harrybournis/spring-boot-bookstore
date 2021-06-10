package com.example.bookstore.exception;

import lombok.Getter;
import org.springframework.dao.DataIntegrityViolationException;

@Getter
public class UniqueConstraintException extends DbException {
  private final String exceptionMessage;

  public UniqueConstraintException(String exceptionMessage, DataIntegrityViolationException e) {
    super(e);
    this.exceptionMessage = exceptionMessage;
  }
}
