package com.example.bookstore.service;

import java.util.List;

import com.example.bookstore.entity.Book;
import com.example.bookstore.errorhandler.DbErrorHandler;
import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.exception.DbException;
import com.example.bookstore.exception.UniqueConstraintException;
import com.example.bookstore.repository.BookRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Validated
public class BookService implements EntityService<Book> {
  @Autowired
  BookRepository bookRepository;

  @Autowired
  DbErrorHandler dbErrorHandler;

  public List<Book> getAll() {
    return bookRepository.allVisiblePublishedSortedByPosition();
  }

  public Book find(Long id) throws BookNotFoundException {
    return bookRepository.findByIdEagerLoad(id).orElseThrow(() -> new BookNotFoundException(id));
  }

  public Book save(@Valid Book book) throws ApiException {
    try {
      return dbErrorHandler.handleSaveExceptions(() ->
              bookRepository.save(book)
      );
    } catch (UniqueConstraintException e) {
      if (isIsbnError(e)) {
        throw new ApiException("book.isbn.must_be_unique", HttpStatus.BAD_REQUEST);
      }
      throw e.toApiException();
    } catch (DbException e) {
      throw e.toApiException();
    }
  }

  @Override
  public void delete(Book book) {
    bookRepository.delete(book);
  }

  private boolean isIsbnError(UniqueConstraintException e) {
    return StringUtils.contains(e.getExceptionMessage(), "isbn");
  }
}
