package com.example.bookstore.service;

import com.example.bookstore.error.exception.ApiException;
import com.example.bookstore.error.exception.PublisherNotFoundException;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.repository.PublisherRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
@Value
public class PublisherService {
  @Autowired
  PublisherRepository publisherRepository;

  public List<Publisher> getAll() {
    return publisherRepository.findAll();
  }

  public Publisher find(Long id) throws PublisherNotFoundException {
    return publisherRepository.findById(id).orElseThrow(() -> new PublisherNotFoundException(id));
  }

  public Publisher save(@Valid Publisher publisher) throws ApiException {
    validateNameUniqueness(publisher.getName(), publisher.getId());
    return publisherRepository.save(publisher);
  }

  public void delete(Publisher publisher) {
    publisherRepository.delete(publisher);
  }

  private void validateNameUniqueness(String email, Long id) throws ApiException {
    if (!publisherRepository.isNameUnique(email, id != null ? id : 0L)) {
      throw new ApiException("publisher.name.must_be_unique", HttpStatus.BAD_REQUEST);
    }
  }
}
