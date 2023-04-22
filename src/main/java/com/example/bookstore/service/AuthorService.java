package com.example.bookstore.service;

import java.util.List;

import com.example.bookstore.entity.Author;
import com.example.bookstore.errorhandler.DbErrorHandler;
import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.AuthorNotFoundException;
import com.example.bookstore.exception.DbException;
import com.example.bookstore.exception.UniqueConstraintException;
import com.example.bookstore.repository.AuthorRepository;

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
public class AuthorService implements EntityService<Author> {
  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  DbErrorHandler dbErrorHandler;

  public List<Author> getAll() {
    return authorRepository.findAll();
  }

  public Author find(Long id) throws AuthorNotFoundException {
    return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
  }

  public Author save(@Valid Author author) throws ApiException {
    try {
      return dbErrorHandler.handleSaveExceptions(() ->
              authorRepository.save(author)
      );
    } catch (UniqueConstraintException e) {
      if (isEmailError(e)) {
        throw new ApiException("author.email.must_be_unique", HttpStatus.BAD_REQUEST);
      }
      throw e.toApiException();
    } catch (DbException e) {
      throw e.toApiException();
    }
  }

  public void delete(Author author) {
    authorRepository.delete(author);
  }

  private boolean isEmailError(UniqueConstraintException e) {
    return StringUtils.contains(e.getExceptionMessage(), "email");
  }
}
