package com.example.bookstore.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Getter
public class ApiException extends Exception {
  private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
  private Map<String, Object> extra;
  private List<String> errorCodes;
  private Object[] parameters;

  public ApiException(List<String> errorCodes, HttpStatus status) {
    super(errorCodes.get(0));
    this.status = status;
    this.errorCodes = errorCodes;
  }

  public ApiException(String errorCode, HttpStatus status) {
    super(errorCode);
    this.status = status;
    this.errorCodes = List.of(errorCode);
  }

  public ApiException(String errorCode, HttpStatus status, Object[] parameters) {
    super(errorCode);
    this.status = status;
    this.errorCodes = List.of(errorCode);
    this.parameters = parameters;
  }

  public ApiException(String errorCode) {
    super(errorCode);
    this.errorCodes = List.of(errorCode);
  }

  public ApiException(Exception exception) {
    super(exception.getMessage());
  }
}
