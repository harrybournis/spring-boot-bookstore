package com.example.bookstore.error;

import com.example.bookstore.error.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
@Value
public class Handler {
  Logger logger = LoggerFactory.getLogger(Handler.class);

  @Autowired
  MessageSource messageSource;

  @Autowired
  Clock clock;

  @Autowired
  Environment environment;

  @ExceptionHandler(ApiException.class)
  protected ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
    logException(ex);
    List<Error> errors = ex.getErrorCodes().stream().map(code ->
            new Error(code, translateErrorCode(code, ex.getParameters(), ex.getMessage()))
    ).collect(Collectors.toList());

    return buildResponseEntity(
            ex.getStatus(),
            new ErrorResponse(clock.instant(), errors, ex.getMessage())
    );
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected ResponseEntity<ErrorResponse> handledd(HttpMessageNotReadableException ex) {
    logException(ex);
    String message = ex.getCause().getMessage();
    String code = "json_parse_failed";
    Error error = new Error(code, translateErrorCode(code, new Object[] { message }, message));

    return buildResponseEntity(
            HttpStatus.BAD_REQUEST,
            new ErrorResponse(clock.instant(), List.of(error), ex.getMessage())
    );
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
    logException(ex);
    List<Error> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      String object = violation.getRootBeanClass().getSimpleName().toLowerCase();
      String code = buildErrorCode(object, violation.getPropertyPath().toString(), violation.getMessage());

      errors.add(new Error(code, translateErrorCode(code, violation.getMessage())));
    }

    return buildResponseEntity(
            HttpStatus.BAD_REQUEST,
            new ErrorResponse(clock.instant(), errors, ex.getMessage())
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    logException(ex);
    List<Error> errors = ex.getAllErrors().stream().map((e) -> {
      FieldError error = (FieldError) e;
      String defaultMessage = error.getDefaultMessage();
      String code = buildErrorCode(error.getObjectName(), error.getField(), defaultMessage);
      return new Error(code, translateErrorCode(code, defaultMessage));
    }).collect(Collectors.toList());

    return buildResponseEntity(
            HttpStatus.BAD_REQUEST,
            new ErrorResponse(clock.instant(), errors, ex.getMessage())
    );
  }

  @ExceptionHandler({Exception.class, DataIntegrityViolationException.class})
  protected ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
    logException(ex);

    return buildResponseEntity(
            HttpStatus.INTERNAL_SERVER_ERROR,
            new ErrorResponse(clock.instant(), List.of(genericError(ex.getMessage())), ex.getMessage())
    );
  }

  private String buildErrorCode(String objectName, String field, String message) {
    return objectName + "." + field + "." + message.replace(" ", "_");
  }

  private Error genericError(String message) {
    return new Error("something_went_wrong", "Something went wrong: " + message);
  }

  private String translateErrorCode(String code) {
    return translateErrorCode(code, "Something went wrong");
  }

  private String translateErrorCode(String code, String defaultMessage) {
    return translateErrorCode(code, null, defaultMessage);
  }

  private String translateErrorCode(String code, Object[] parameters, String defaultMessage) {
    return messageSource.getMessage("errors." + code, parameters, defaultMessage, new Locale(""));
  }

  private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, ErrorResponse errorResponse) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<>(errorResponse, headers, status);
  }

  private void logException(Exception ex) {
    ex.printStackTrace();
  }
}
