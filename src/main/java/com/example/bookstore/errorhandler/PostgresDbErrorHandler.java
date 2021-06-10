package com.example.bookstore.errorhandler;

import com.example.bookstore.exception.DbException;
import com.example.bookstore.exception.UniqueConstraintException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.function.Supplier;

public class PostgresDbErrorHandler implements DbErrorHandler {
  public <T> T handleSaveExceptions(Supplier<T> r) throws DbException {
    DbException exception = null;

    try {
      return r.get();
    } catch (DataIntegrityViolationException e) {
      exception = handle(e);
    } catch (Exception e)  {
      exception = handle(e);
    }

    throw exception;
  }

  private DbException handle(DataIntegrityViolationException e) {
    String message = getRootCauseMessage(e);

    if (isUniquenessError(message)) {
      return new UniqueConstraintException(message, e);
    } else {
      return wrapException(e);
    }
  }

  private DbException handle(Exception e) {
    return wrapException(e);
  }

  private DbException wrapException(Exception e) {
    return new DbException(e);
  }

  private boolean isUniquenessError(String message) {
    return StringUtils.contains(message, "ERROR: duplicate key value violates unique constraint");
  }

  private String getRootCauseMessage(DataIntegrityViolationException e) {
    if (e.getRootCause() == null) {
      return null;
    }
    return e.getRootCause().getMessage();
  }
}
