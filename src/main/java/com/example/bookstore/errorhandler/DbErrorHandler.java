package com.example.bookstore.errorhandler;

import com.example.bookstore.exception.DbException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public interface DbErrorHandler {
  <T> T handleSaveExceptions(Supplier<T> r) throws DbException;
}
