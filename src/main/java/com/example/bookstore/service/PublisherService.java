package com.example.bookstore.service;

import com.example.bookstore.entity.Publisher;
import com.example.bookstore.errorhandler.DbErrorHandler;
import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.DbException;
import com.example.bookstore.exception.PublisherNotFoundException;
import com.example.bookstore.exception.UniqueConstraintException;
import com.example.bookstore.repository.PublisherRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class PublisherService implements EntityService<Publisher> {
  @Autowired
  PublisherRepository publisherRepository;

  @Autowired
  DbErrorHandler dbErrorHandler;

  public List<Publisher> getAll() {
    return publisherRepository.findAll();
  }

  public Publisher find(Long id) throws PublisherNotFoundException {
    return publisherRepository.findById(id).orElseThrow(() -> new PublisherNotFoundException(id));
  }

  public Publisher save(@Valid Publisher publisher) throws ApiException {
    try {
      return dbErrorHandler.handleSaveExceptions(() ->
              publisherRepository.save(publisher)
      );
    } catch (UniqueConstraintException e) {
      if (isNameError(e)) {
        throw new ApiException("publisher.name.must_be_unique", HttpStatus.BAD_REQUEST);
      }
      throw e.toApiException();
    } catch (DbException e) {
      throw e.toApiException();
    }
  }

  public void delete(Publisher publisher) {
    publisherRepository.delete(publisher);
  }

  private boolean isNameError(UniqueConstraintException e) {
    return StringUtils.contains(e.getExceptionMessage(), "name");
  }
}
